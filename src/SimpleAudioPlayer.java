import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
  
public class SimpleAudioPlayer 
{
    // to store current position
    Long currentFrame;
    Clip clip;
    // current status of clip
    AudioInputStream audioInputStream;
    // constructor to initialize streams and clip
    public SimpleAudioPlayer() throws UnsupportedAudioFileException,IOException, LineUnavailableException 
    {
        // create AudioInputStream object
        audioInputStream = AudioSystem.getAudioInputStream(new File("catmint.wav").getAbsoluteFile());
        // create clip reference
        clip = AudioSystem.getClip();
          
        // open audioInputStream to the clip
        clip.open(audioInputStream);
          
        //clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
  
    public static void main(String[] args) 
    {
        try
        {
            SimpleAudioPlayer audioPlayer = new SimpleAudioPlayer();
            audioPlayer.play();
            Scanner sc = new Scanner(System.in);
              
            while (true)
            {
                int c = sc.nextInt();
                if (c == 4)
                break;
            }
            sc.close();
        } 
        catch (Exception ex) 
        {
        }
    }
    // Method to play the audio
    public void play() 
    {
        //start the clip
        clip.start();
    }
      
}