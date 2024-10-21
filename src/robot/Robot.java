package robot;

public record Robot (
        boolean isSmiling,
        boolean hasTie,
        Body body,
        Head head,
        ItemInHand itemInHand
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot robot = (Robot) o;
        return isSmiling == robot.isSmiling
                && hasTie == robot.hasTie
                && body == robot.body
                && head == robot.head
                && itemInHand == robot.itemInHand;
    }
}
