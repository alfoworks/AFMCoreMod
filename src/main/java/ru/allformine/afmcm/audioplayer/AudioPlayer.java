package ru.allformine.afmcm.audioplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class AudioPlayer {
    private InputStream stream;
    private IntBuffer source;
    private IntBuffer buffer;

    private boolean playing = false;

    private boolean alError() {
        if (AL10.alGetError() != AL10.AL_NO_ERROR) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("An AL error occurred when was trying to play background music."));
            return true;
        }
        return false;
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    }

    public AudioPlayer(InputStream stream) {
        this.stream = stream;
    }

    public void play() throws IOException {
        AudioInputStream in = (AudioInputStream) this.stream;

        final AudioFormat outFormat = getOutFormat(in.getFormat());

        this.source = BufferUtils.createIntBuffer(1);

        AL10.alGenSources(this.source);
        if (alError()) {
            close();
            return;
        }

        AL10.alSourcei(this.source.get(0), AL10.AL_LOOPING, AL10.AL_TRUE);
        AL10.alSourcef(this.source.get(0), AL10.AL_PITCH, 1.0f);
        AL10.alSourcef(this.source.get(0), AL10.AL_GAIN, 0.0F * Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.RECORDS));

        if (alError()) {
            close();
            return;
        }

        this.playing = true;

        stream(AudioSystem.getAudioInputStream(outFormat, in));

        if (this.playing) {
            while (AL10.alGetSourcei(this.source.get(0), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        close();
    }

    private void stream(AudioInputStream in) throws IOException {
        AudioFormat format = in.getFormat();

        byte[] databuffer = new byte[65536];
        for (int n = 0; this.playing && n != -1; n = in.read(databuffer, 0, databuffer.length)) {
            if (n > 0) {
                if (this.buffer == null) {
                    this.buffer = BufferUtils.createIntBuffer(1);
                } else {
                    int processed = AL10.alGetSourcei(this.source.get(0), AL10.AL_BUFFERS_PROCESSED);
                    if (processed > 0) {
                        AL10.alSourceUnqueueBuffers(this.source.get(0), this.buffer);
                        alError();
                    }
                }

                AL10.alGenBuffers(this.buffer);
                ByteBuffer data = (ByteBuffer) BufferUtils.createByteBuffer(n).order(ByteOrder.LITTLE_ENDIAN).put(databuffer, 0, n).flip();
                AL10.alBufferData(this.buffer.get(0), (format.getChannels() > 1) ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16, data, (int) format.getSampleRate());
                alError();
                AL10.alSourceQueueBuffers(this.source.get(0), buffer);

                int state = AL10.alGetSourcei(this.source.get(0), AL10.AL_SOURCE_STATE);
                if (this.playing && state != AL10.AL_PLAYING) {
                    AL10.alSourcePlay(this.source.get(0));
                }
            }
        }
    }

    public void close() {
        this.playing = false;

        if (this.source != null) {
            AL10.alSourceStop(this.source);
            AL10.alDeleteSources(this.source);
            this.source = null;
        }
        if (this.buffer != null) {
            AL10.alDeleteBuffers(this.buffer);
            this.buffer = null;
        }
    }
}
