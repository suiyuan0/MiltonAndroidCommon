add sws support

./configure --disable-altivec --disable-amd3dnow --disable-amd3dnowext \
--disable-avdevice --disable-postproc \
--disable-pthreads --disable-avfilter \
--disable-doc --disable-ffmpeg --disable-ffplay --disable-ffprobe --disable-ffserver \
--disable-pthreads --disable-w32threads --disable-os2threads \
--disable-encoder=aac \
--disable-decoder=aac \
--disable-decoder=aac_latm \
--disable-decoder=aasc \
--cross-prefix=arm-linux-androideabi- --target-os=linux --arch=arm \
--prefix=/media/androiddev/ffmpeg-2.1.4-android1
