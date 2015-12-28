package topcoder.topcoder.anheuser.view.data.main;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public abstract class MainTile {
    private String title;

    public String getTitle() {
        return title;
    }

    public MainTile setTitle(String title) {
        this.title = title;
        return this;
    }
}
