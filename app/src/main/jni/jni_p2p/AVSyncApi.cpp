//
// Created by e562450 on 11/27/2015.
//

#include "AVSyncApi.hh"

#ifdef __MACH__
int clock_gettime(int clk_id, struct timespec *t){
    mach_timebase_info_data_t timebase;
    mach_timebase_info(&timebase);
    uint64_t time;
    time = mach_absolute_time();
    double nseconds = ((double)time * (double)timebase.numer)/((double)timebase.denom);
    double seconds = ((double)time * (double)timebase.numer)/((double)timebase.denom * 1e9);
    t->tv_sec = seconds;
    t->tv_nsec = nseconds;
    return 0;
}
int sem_timedwait(sem_t *sem, const struct timespec *abs_timeout)
{
    struct timeval timenow;
    struct timespec sleepytime;
    int retcode;

    /* This is just to avoid a completely busy wait */
    sleepytime.tv_sec = 0;
    sleepytime.tv_nsec = 10000000; /* 10ms */

    while ((retcode = sem_trywait(sem)) == -1)
    {
        gettimeofday (&timenow, NULL);

        if (timenow.tv_sec >= abs_timeout->tv_sec &&
            (timenow.tv_usec * 1000) >= abs_timeout->tv_nsec)
        {
            return -1;
        }
        nanosleep (&sleepytime, NULL);
    }

    return 0;
}
#endif