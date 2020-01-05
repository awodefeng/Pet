package com.xxun.watch.xunpet.jni;

public class SoundStretchJNI {
	  public static native long newSoundStretch();
	  public static native void deleteSoundStretch(long jarg1);
	  public static native void processSoundStretch(long jarg1, SoundStretch jarg1_, String jarg2, String jarg3, float jarg4, float jarg5, float jarg6);
}
