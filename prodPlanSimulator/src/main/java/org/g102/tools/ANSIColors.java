package org.g102.tools;

public enum ANSIColors {

        //--Text Style--
        ANSI_RESET(0),
        ANSI_BOLD(1),
        ANSI_ITALIC(3),
        ANSI_UNDERLINE(4),
        ANSI_BLINK(5),
        ANSI_REVERSE(7),
        ANSI_CONCEAL(8),

        //--Text Color--
        ANSI_BLACK(30),
        ANSI_RED(31),
        ANSI_GREEN(32),
        ANSI_YELLOW(33),
        ANSI_BLUE(34),
        ANSI_PURPLE(35),
        ANSI_CYAN(36),
        ANSI_WHITE(37),

        //--Background Color--
        ANSI_BLACK_BACKGROUND(40),
        ANSI_RED_BACKGROUND(41),
        ANSI_GREEN_BACKGROUND(42),
        ANSI_YELLOW_BACKGROUND(43),
        ANSI_BLUE_BACKGROUND(44),
        ANSI_PURPLE_BACKGROUND(45),
        ANSI_CYAN_BACKGROUND(46),
        ANSI_WHITE_BACKGROUND(47),

        //--Light Colors--
        ANSI_LIGHT_GREEN(92),
        ANSI_LIGHT_YELLOW(93);

        private final String ansiCode;

        private ANSIColors(int code) {
            this.ansiCode = "\033[" + code + "m";
        }

        public static String paint(String text, ANSIColors... colors) {
            StringBuilder sb = new StringBuilder();
            for (ANSIColors color : colors) {
                sb.append(color.ansiCode);
            }
            sb.append(text);
            sb.append(ANSI_RESET.ansiCode);

            return sb.toString();
        }
}
