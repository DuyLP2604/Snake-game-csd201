import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Objects;

public class Sound {
    private static Clip bgClip;
    public static void play(String file) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(Sound.class.getResource("/resources/sound/" + file));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void playBackground(String file) {
        try {
            stopBackground();
            AudioInputStream audio = AudioSystem.getAudioInputStream(Sound.class.getResource("/resources/sound/" + file));

            bgClip = AudioSystem.getClip();
            bgClip.open(audio);

            bgClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopBackground() {
        if (bgClip != null) {
            bgClip.stop();
            bgClip.close();
            bgClip = null;
        }
    }
}
