import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Produto {

    // Inserir
    public void inserirProduto(String nome, String marca, double preco) {
        String sql = "INSERT INTO Produtos (nome_produto, marca_produto, preco_produto) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, marca);
            stmt.setDouble(3, preco);
            stmt.executeUpdate();

            System.out.println("✅ Produto inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
        }
    }

    // Excluir
    public void excluirProduto(int idProduto) {
        String sql = "DELETE FROM Produtos WHERE id_produto = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("✅ Produto excluído com sucesso!");
            } else {
                System.out.println("⚠ Nenhum produto encontrado com esse ID.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
        }
    }

    // Listar
    public List<String> listarProdutos() {
        List<String> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos";

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String produto = rs.getInt("id_produto") + " - " +
                                 rs.getString("nome_produto") + " - " +
                                 rs.getString("marca_produto") + " - " +
                                 rs.getDouble("preco_produto");
                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }
}
