package boustrophedon.domain.graph.error;

public class ElementNotFoundedException extends Exception{
        public ElementNotFoundedException(Object element) {
            super("The element is not in the ArrayList: " + element.toString());
        }
    public ElementNotFoundedException() {
        super("The element is not in the ArrayList");
    }
}
