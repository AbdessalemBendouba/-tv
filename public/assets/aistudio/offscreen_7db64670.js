let audioInstance = null;

chrome.runtime.onMessage.addListener((msg) => {
  console.log("[Offscreen] Received message payload:", msg);
  
  if (msg.action === 'play_athan' || msg.action === 'test_athan') {
    try {
      // 1. Stop any active playback if a new trigger is requested
      if (audioInstance) {
        console.log("[Offscreen] Stopping previous audio stream.");
        audioInstance.pause();
        audioInstance = null;
      }

      // 2. Parse values safely
      const file = msg.file || 'athan.mp3';
      const volume = (msg.volume !== undefined && !isNaN(Number(msg.volume))) ? Number(msg.volume) : 0.5;
      const testCutoffSecs = 100; // FIXED: Elevated to main function scope to prevent Chromium 109 block-scope closures bugs !

      console.log(`[Offscreen] Instantiating Audio: ${file} | Target Volume: ${volume}`);

      // 3. Setup HTML5 Audio Node
      audioInstance = new Audio(file);
      audioInstance.volume = volume;

      // Close the offscreen document safely when the Athan finishes playing
      audioInstance.onended = () => {
        console.log("[Offscreen] Audio playback ended naturally. Terminating document.");
        window.close();
      };

      // 4. Handle manual test cutoff timer
      if (msg.isTest) {
        console.log(`[Offscreen] Test mode active. Scheduling automatic ${testCutoffSecs}-Seconds cutoff timer.`);
        
        setTimeout(() => {
          if (audioInstance) {
            console.log("[Offscreen] 3-minute volume test limit reached. Stopping audio.");
            audioInstance.pause();
            window.close();
          }
        }, testCutoffSecs  * 1000); 
      }

      // 5. Trigger playback
      audioInstance.play().catch(err => {
        console.error("[Offscreen] Playback exception caught:", err);
        window.close(); // Clean close on crash
      });

    } catch (err) {
      console.error("[Offscreen] Critical execution error:", err);
      window.close();
    }
  }
});