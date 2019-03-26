package generators;

public class Driver {
	public static void main(String[] args) throws Exception {
		new InterfaceGenerator();
		new ClassGenerator();
		new ProxyGenerator();
	}
}
