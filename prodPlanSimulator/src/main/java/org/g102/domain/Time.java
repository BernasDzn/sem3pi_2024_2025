package org.g102.domain;

public class Time {
    private int hours;
    private int minutes;
    private int seconds;

    /**
     * Constructor for Time
     * @param hours
     * @param minutes
     * @param seconds
     */
    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * Constructor for Time
     * @param seconds
     */
    public Time(int seconds) {
        this.hours = seconds / 3600;
        this.minutes = (seconds % 3600) / 60;
        this.seconds = seconds % 60;
    }

    /**
     * Hours getter
     * @return hours
     */
    public int toSeconds() {
        return hours * 3600 + minutes * 60 + seconds;
    }

    /**
     * Hours getter
     * @return hours
     */
    public int getHours() {
        return hours;
    }

    /**
     * Minutes getter
     * @return minutes
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Seconds getter
     * @return seconds
     */
    public int getSeconds() {
        return seconds;
    }

    public int getAllToMinutes() {
        return hours * 60 + minutes + seconds / 60;
    }

    /**
     * Prints the time in the format HH:MM:SS if hours and minutes are not 0,
     * MM:SS if hours is 0 and SS if hours and minutes are 0
     * @return time formated as a string
     */
    @Override
    public String toString() {
        if (hours == 0 && minutes == 0) {
            return String.format("%02d", seconds);
        } else if (hours == 0) {
            return String.format("%02d:%02d", minutes, seconds);
        } else {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }
}
