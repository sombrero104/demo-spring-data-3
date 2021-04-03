package me.demospringdata3.post;

/**
 * [ Projection ]
 * 클래스 버전.
 */
public class CommentSummary2 {

    private String comment;

    private int up;

    private int down;

    public CommentSummary2(String comment, int up, int down) {
        this.comment = comment;
        this.up = up;
        this.down = down;
    }

    public String getComment() {
        return comment;
    }

    public int getUp() {
        return up;
    }

    public int getDown() {
        return down;
    }

    public String getVotes() {
        return this.up + " " + this.down;
    }

}
