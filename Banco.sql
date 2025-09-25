CREATE TABLE Funcionario (
	id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    cpf CHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(50) NOT NULL,
    cargo VARCHAR(50),
	salario DECIMAL(10,2),
    telefone VARCHAR(20)
); 

CREATE TABLE Cliente(
	id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) UNIQUE NOT NULL,
	telefone VARCHAR(20)
);

CREATE TABLE Servicos(
	id_Servico INT AUTO_INCREMENT PRIMARY KEY,
	nome_servico VARCHAR(50) NOT NULL,
	preco_servico DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Produtos(
	id_Produto INT AUTO_INCREMENT PRIMARY KEY,
	nome_produto VARCHAR(50),
	marca_produto VARCHAR(50),
	preco_produto DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Venda(
	id_venda INT AUTO_INCREMENT PRIMARY KEY,
    data_venda DATE NOT NULL,
    id_cliente INT NOT NULL,
    id_funcionario INT NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL DEFAULT 0,
    produtos TEXT,
    servicos TEXT,
    
    FOREIGN KEY(id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY(id_funcionario) REFERENCES Funcionario(id_funcionario)
);