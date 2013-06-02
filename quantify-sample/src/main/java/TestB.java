public class TestB {

    public void run(String s, int i) throws Exception {
        Thread.sleep(250L);
        new TestC().run();
    }

}
