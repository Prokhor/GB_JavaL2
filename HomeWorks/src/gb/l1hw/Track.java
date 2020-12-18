package gb.l1hw;

public class Track implements Actionable {

    private int distance;

    public Track(int distance) {
        this.distance = distance;
    }

    @Override
    public void doAction(Functionalable fnc){
        fnc.run(distance);
    }
}
