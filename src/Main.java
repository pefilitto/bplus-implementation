public class Main {
    public static void main(String[] args) {
        Arvore arv = new Arvore(3);

        for (int i = 0; i < 10000; i++) {
            arv.inserir(i);
        }

        arv.in_ordem();

        for (int i = 150; i <= 300; i++) {
            arv.excluir(i);
        }

//        arv.inserir(1);
//        arv.inserir(4);
//        arv.inserir(7);
//        arv.inserir(10);
//        arv.inserir(17);
//        arv.inserir(21);
//        arv.inserir(31);
//        arv.inserir(25);
//        arv.inserir(19);
//        arv.inserir(20);
//        arv.inserir(28);
//        arv.inserir(42);
//
//        arv.in_ordem();

        System.out.println("Apos exclusao");

//        arv.excluir(28);
//        arv.excluir(19);
//        arv.excluir(31);

        arv.in_ordem();
    }
}