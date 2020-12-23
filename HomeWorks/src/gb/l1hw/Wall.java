package gb.l1hw;

public class Wall implements Actionable {

    private double height;

    public Wall(double height) {
        this.height = height;
    }

    @Override
    public void doAction(Functionalable fnc){
        fnc.jump(height);
    }
}
