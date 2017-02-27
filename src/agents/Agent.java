package agents;

public interface Agent {
    void init(String role, int width, int height, int playclock);
    String nextAction(int[] lastmove);
    void cleanup();
}
