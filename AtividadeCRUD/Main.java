import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Instanciar o Database e o repositório
        Database database = new Database("lavanderia.db");
        TransactionRepository repository = new TransactionRepository(database);

        // Lista de máquinas (dinâmica)
        List<MaquinaLavar> maquinas = new ArrayList<>();
        maquinas.add(new MaquinaPequena("MAQ5"));    // Preço 10.0
        maquinas.add(new MaquinaMedia("MAQ6"));      // Preço 15.0
        maquinas.add(new MaquinaPesada("MAQ7"));     // Preço 20.0

        // Lista de matrículas para teste
        int[] matriculas = {12345, 67890, 11111};

        // Adicionar transações para cada máquina
        for (int i = 0; i < maquinas.size(); i++) {
            MaquinaLavar maquina = maquinas.get(i);
            int matricula = matriculas[i % matriculas.length];

            System.out.println("Testando cobrança para " + maquina.getClass().getSimpleName() + " (ID: " + maquina.getId() + ")...");
            Caixa.Transaction transacao = new Caixa.Transaction(matricula, maquina.getPreco());
            repository.create(transacao);
            System.out.println("Transação registrada no banco para matrícula " + matricula + " com valor R$ " + String.format("%.2f", maquina.getPreco()));
        }

        // Carregar e exibir todas as transações
        List<Caixa.Transaction> transacoes = repository.loadAll();
        System.out.println("Transações no banco: " + transacoes.size());
        for (Caixa.Transaction t : transacoes) {
            System.out.println("Matrícula: " + t.getMatricula() + ", Valor: R$ " + t.getValor() + ", Data: " + t.getData());
        }

        // Fechar a conexão
        repository.close();
        System.out.println("Teste concluído. Verifique o banco de dados.");
    }
}
