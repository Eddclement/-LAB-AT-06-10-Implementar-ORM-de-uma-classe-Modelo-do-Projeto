import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class TransactionRepository {
    private static Database database;
    private static Dao<Caixa.Transaction, Integer> dao;
    private List<Caixa.Transaction> loadedTransactions;
    private Caixa.Transaction loadedTransaction;

    public TransactionRepository(Database database) {
        TransactionRepository.setDatabase(database);
        loadedTransactions = new ArrayList<Caixa.Transaction>();
    }

    public static void setDatabase(Database database) {
        TransactionRepository.database = database;
        try {
            dao = DaoManager.createDao(database.getConnection(), Caixa.Transaction.class);
            // Cria a tabela se não existir (fallback, já que você a criou manualmente)
            TableUtils.createTableIfNotExists(database.getConnection(), Caixa.Transaction.class);
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar TransactionRepository: " + e.getMessage());
            System.exit(0);
        }
    }

    public Caixa.Transaction create(Caixa.Transaction transaction) {
        int nrows = 0;
        try {
            nrows = dao.create(transaction);
            if (nrows == 0) {
                throw new SQLException("Erro: transação não salva");
            }
            this.loadedTransaction = transaction;
            loadedTransactions.add(transaction);
        } catch (SQLException e) {
            System.err.println("Erro ao criar transação: " + e.getMessage());
        }
        return transaction;
    }

    public List<Caixa.Transaction> loadAll() {
        try {
            this.loadedTransactions = dao.queryForAll();
            if (!this.loadedTransactions.isEmpty()) {
                this.loadedTransaction = this.loadedTransactions.get(0);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar transações: " + e.getMessage());
        }
        return this.loadedTransactions;
    }

    public void close() {
        database.close();
    }
}
