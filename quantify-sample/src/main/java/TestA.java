public class TestA {

    public static void main(final String... args) throws Exception {
        new TestA().run();
    }

    public void run() throws Exception {
        Thread.sleep(100L);
        new TestB().run("abc", 123);
    }

}
