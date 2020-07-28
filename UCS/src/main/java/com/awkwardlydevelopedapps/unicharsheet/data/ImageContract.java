package com.awkwardlydevelopedapps.unicharsheet.data;

import com.awkwardlydevelopedapps.unicharsheet.R;

public final class ImageContract {
    //Character's images
    public final static class Character {
        //120dp in Android Asset Studio
        public final static int COWLED_ID = R.drawable.ic_cowled;
        public final static int CULTIST_ID = R.drawable.ic_cultist;
        public final static int VIKING_ID = R.drawable.ic_viking_head;
        public final static int WIZARD_ID = R.drawable.ic_wizard_face;
        public final static int VISORED_ID = R.drawable.ic_visored_helm;
        public final static int KENAKU_ID = R.drawable.ic_kenku_head;

        public final static String COWLED = "cowled";
        public final static String CULTIST = "cultist";
        public final static String VIKING = "viking";
        public final static String WIZARD = "wizard";
        public final static String VISORED = "visored";
        public final static String KENAKU = "kenaku";
    }

    public final static class Spell {
        public final static int FIRE_ID = R.drawable.fire_zone_hex;
        public final static int AIR_ID = R.drawable.fluffy_cloud_hex;
        public final static int WATER_ID = R.drawable.splashy_stream_hex;
        public final static int EARTH_ID = R.drawable.stone_pile_hex;
        public final static int NATURE_ID = R.drawable.three_leaves_hex;
        public final static int ESSENCE_ID = R.drawable.jawbone_hex;
        public final static int MIND_ID = R.drawable.sunken_eye_hex;

        public final static String FIRE = "fire";
        public final static String AIR = "air";
        public final static String WATER = "water";
        public final static String EARTH = "earth";
        public final static String NATURE = "nature";
        public final static String ESSENCE = "essence";
        public final static String MIND = "mind";
    }
}
