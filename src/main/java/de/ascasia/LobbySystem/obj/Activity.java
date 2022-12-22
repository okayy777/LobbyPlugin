package de.ascasia.LobbySystem.obj;

public enum Activity {
    SwingMainArm(0),
    TakeDamage(1),
    LeaveBed(2),
    SwingOffArm(3),
    CriticalEffect(4),
    MagicCriticalEffect(5);

    private final int ID;

    private Activity(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }
    public static Activity ActivityFromInt(int ID) {
        if (ID <= 5) {
            switch (ID) {
                case 0:
                    return Activity.SwingMainArm;
                case 1:
                    return Activity.TakeDamage;
                case 2:
                    return Activity.LeaveBed;
                case 3:
                    return Activity.SwingOffArm;
                case 4:
                    return Activity.CriticalEffect;
                case 5:
                    return Activity.MagicCriticalEffect;
            }
        }
        return null;
    }

}
