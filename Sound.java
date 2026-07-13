import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Objects;

public class Sound {
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
}
