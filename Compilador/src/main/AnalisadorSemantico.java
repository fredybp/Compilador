package main;

import java.util.ArrayList;
import java.util.List;
import static main.Operadores.*;
import static main.AnalisadorSintatico.*;

import static main.GeradorCodigo.*;



public class AnalisadorSemantico {

	public static final String NOME_DE_VARIAVEL = "nomedevariavel";
	public static final String NOME_DE_PROCEDIMENTO = "nomedeprocedimento";
	public static final String NOME_DE_FUNCAO = "nomedefuncao";

	public static List<Simbolo> tabelaSimbolos = new ArrayList<>();

	public static List<Token> pilhaPosfixo = new ArrayList<>();
	public static List<Token> filaPosfixo = new ArrayList<>();

	public static void zeraVariaveis() {
		tabelaSimbolos.clear();
		pilhaPosfixo.clear();
		filaPosfixo.clear();
	}

	private static int getUltimaPosicaoLista() {
		return tabelaSimbolos.size() - 1;
	}

	public static List<Simbolo> getTabelaSimbolos() {
		return tabelaSimbolos;
	}

	public static void desempilhaPilhaParenteses() {
		int i = pilhaPosfixo.size() - 1;
		while (i >= 0) {
			if (!pilhaPosfixo.get(i).getLexema().equals("(")) {
				adicionaFilaPosfixo(pilhaPosfixo.remove(i));
			} else {
				pilhaPosfixo.remove(i);
				break;
			}
			i--;
		}
	}

	public static Simbolo getSimboloTopoTabela() {
		return tabelaSimbolos.get(getUltimaPosicaoLista());
	}

	public static void adicionaFilaPosfixo(Token token) {
		filaPosfixo.add(token);
	}

	public static void adicionaPilhaPosfixo(Token token) {
		int i = pilhaPosfixo.size() - 1;
		int predenciaParametro = token.isUnario() ? getPrecedenciaOperadores("unario")
				: getPrecedenciaOperadores(token.getLexema());
		int predenciaPilha;

		if ("(".equals(token.getLexema())) {
			pilhaPosfixo.add(token);
		} else {
			while (i >= 0) {
				predenciaPilha = getPrecedenciaOperadores(pilhaPosfixo.get(i).getLexema());
				if (predenciaPilha >= predenciaParametro) {
					adicionaFilaPosfixo(pilhaPosfixo.remove(i));
				} else {
					break;
				}
				i--;
			}
			pilhaPosfixo.add(token);
		}
	}

	public static Simbolo getSimboloVariavelFuncao(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& (NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())
							|| NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()))) {
				return tabelaSimbolos.get(i);
			}
			i--;
		}
		return null;
	}

	public static void insereTabelaSimbolos(String lexema, String tipo, Integer nivel, String rotulo,
			String tipoLexema, Integer endereco) {
		Simbolo simbolo = new Simbolo();
		simbolo.setLexema(lexema);
		simbolo.setTipo(tipo);
		simbolo.setNivel(nivel);
		simbolo.setRotulo(rotulo);
		simbolo.setTipoLexema(tipoLexema);
		simbolo.setEndereco(endereco);
		tabelaSimbolos.add(simbolo);
	}

	public static void colocaTipoVariaveis(String tipo) {
		String tipoVariavel = tipo.substring(1);
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema())
					|| NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				break;
			} else if (tabelaSimbolos.get(i).getTipo() == null
					&& NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				tabelaSimbolos.get(i).setTipo(tipoVariavel);
			}
			i--;
		}
	}

	public static boolean pesquisaDeclaracaoVariavelTabela(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return true;
			}
			i--;
		}
		return false;
	}
	
	public static Integer getEnderecoVariavel(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return tabelaSimbolos.get(i).getEndereco();
			}
			i--;
		}
		return null;
	}
	
	public static String getRotuloProcedimento(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return tabelaSimbolos.get(i).getRotulo();
			}
			i--;
		}
		return null;
	}
	
	public static String getRotuloFuncao(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return tabelaSimbolos.get(i).getRotulo();
			}
			i--;
		}
		return null;
	}


	public static boolean pesquisaDuplicidadeVariavelTabela(String lexema, Integer nivel) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema) && nivel.equals(tabelaSimbolos.get(i).getNivel())) {
				return true;
			}
			i--;
		}
		return verificaVariavelEqualsNomeProcedimentoFuncao(lexema);
	}

	private static boolean verificaVariavelEqualsNomeProcedimentoFuncao(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& (NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())
							|| NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()))) {
				return true;
			}
			i--;
		}
		return false;
	}

	public static boolean pesquisaDeclaracaoFuncaoVariavelTabela(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& (NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())
					|| NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()))) {
				return true;
			}
			i--;
		}
		return false;
	}
	

	public static boolean pesquisaDeclaracaoProcedimentoTabela(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return true;
			}
			i--;
		}
		return false;
	}

	public static boolean pesquisaDeclaracaoFuncaoTabela(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return true;
			}
			i--;
		}
		return false;
	}

	public static void desempilhaNivelTabela(Integer nivel) {
		
		int contadorVariaveis = 0;
		int ultimoEndereco = 0;
		
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getNivel() == nivel && 
					NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())){
				contadorVariaveis++;
				ultimoEndereco = tabelaSimbolos.get(i).getEndereco();
				enderecoMemoria--;
				tabelaSimbolos.remove(i);
			} else if (tabelaSimbolos.get(i).getNivel() > nivel) {
				tabelaSimbolos.remove(i);
			} else {
				break;
			}
			i--;
		}
		if (contadorVariaveis > 0) {
			gera("DALLOC "+ultimoEndereco+","+contadorVariaveis);
		}
	}

	public static void colocaTipoRetornoFuncao(String tipo) {
		String tipoVariavel = tipo.substring(1);
		tabelaSimbolos.get(getUltimaPosicaoLista()).setTipo(tipoVariavel);
	}

	public static boolean pesquisa_tabela(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return true;
			}
			i--;
		}
		return false;
	}

	public static String getTipoFuncaoVariavel(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema) 
					&& (NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()) ||
							NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema()))) {
				return tabelaSimbolos.get(i).getTipo();
			}
			i--;
		}
		return null;
	}

	public static String analisaPosfixo() throws Exception {
		if (!pilhaPosfixo.isEmpty()) {
			int i = pilhaPosfixo.size() - 1;
			while (i >= 0) {
				adicionaFilaPosfixo(pilhaPosfixo.remove(i));
				i--;
			}
		}

		for (Token token : filaPosfixo) {
			System.out.print(token.getLexema());
		}
		System.out.print("\n");

		List<String> filaCompatibilidadeTipo = new ArrayList<>();

		for (int i = 0; i < filaPosfixo.size(); i++) {
			Token token = filaPosfixo.get(i);
			switch (token.getSimbolo()) {
			case "snumero":
				filaCompatibilidadeTipo.add("inteiro");
				gera("LDC "+token.getLexema());
				break;
			case "sidentificador":
				filaCompatibilidadeTipo.add(pesquisaTipoFuncaoVariavelTabela(token.getLexema()));
				if(pesquisaDeclaracaoFuncaoTabela(token.getLexema())) {
					String rotuloFuncao = getRotuloFuncao(token.getLexema());
					
					gera("CALL "+rotuloFuncao);
					gera("LDV 0");
				} else {
					int enderecoVariavel = getEnderecoVariavel(token.getLexema());
					gera("LDV "+enderecoVariavel);
				}
				break;
			case "sverdadeiro":
				filaCompatibilidadeTipo.add("booleano");
				gera("LDC 1");
				break;
			case "sfalso":
				filaCompatibilidadeTipo.add("booleano");
				gera("LDC 0");
				break;	
			case "snao":
				gera("NEG");
				break;
			case "smais":
				if (token.isUnario()) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
						gera("NULL");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "N�o foi possivel aplicar o un�rio mais (+) em um booleano " + token.getLexema()
								+ ".");
					}
				} else {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
						if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
							filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
							filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "inteiro");
						} else {
							throw new Exception("Erro na linha " + token.getLinha() + ". "
									+ "Opera��o de soma com tipos incompativeis.");
						}
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Opera��o de soma com tipos incompativeis.");
					}
					gera("ADD");
				}
				break;
			case "smenos":
				if (token.isUnario()) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
						gera("INV");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "N�o foi possivel aplicar o un�rio menos (-) em um booleano " + token.getLexema()
								+ ".");
					}
				} else {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
						if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
							filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
							filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "inteiro");
							
							gera("SUB");
						} else {
							throw new Exception("Erro na linha " + token.getLinha() + ". "
									+ "Opera��o de subtra��o com tipos incompat�veis.");
						}
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Opera��o de subtra��o com tipos incompat�veis.");
					}
				}
				break;
			case "smult":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "inteiro");
						
						gera("MULT");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Opera��o de multiplica��o com tipos incompat�veis.");
					}
				} else {
					throw new Exception("Erro na linha " + token.getLinha() + ". "
							+ "Opera��o de multiplica��o com tipos incompat�veis.");
				}
				break;
			case "sdiv":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "inteiro");
						
						gera("DIVI");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Opera��o de divis�o com tipos incompat�veis.");
					}
				} else {
					throw new Exception("Erro na linha " + token.getLinha() + ". "
							+ "Opera��o de divis�o com tipos incompat�veis.");
				}
				break;
			case "smenor":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
						
						gera("CME");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Compara��o de menor com tipos incompat�veis.");
					}
				} else {
					throw new Exception("Erro na linha " + token.getLinha() + ". "
							+ "Compara��o de menor com tipos incompat�veis.");
				}
				break;
			case "smaior":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
						
						gera("CMA");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Compara��o de maior com tipos incompat�veis.");
					}
				} else {
					throw new Exception("Erro na linha " + token.getLinha() + ". "
							+ "Compara��o de maior com tipos incompat�veis.");
				}
				break;
			case "smaiorig":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
						
						gera("CMAQ");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Compara��o de maior ou igual com tipos incompat�veis.");
					}
				} else {
					throw new Exception("Erro na linha " + token.getLinha() + ". "
							+ "Compara��o de maior ou igual com tipos incompat�veis.");
				}
				break;
			case "smenorig":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
						
						gera("CMEQ");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Compara��o de menor ou igual com tipos incompat�veis.");
					}
				} else {
					throw new Exception("Erro na linha " + token.getLinha() + ". "
							+ "Compara��o de menor ou igual com tipos incompat�veis.");
				}
				break;
			case "sig":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Compara��o de igualdade com tipos incompat�veis.");
					}
				} else if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("booleano")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("booleano")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Compara��o de igualdade com tipos incompat�veis.");
					}	
				}
				gera("CEQ");
				break;
			case "sdif":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("inteiro")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("inteiro")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Compara��o de desigualdade com tipos incompat�veis.");
					}
				} else if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("booleano")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("booleano")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Compara��o de desigualdade com tipos incompat�veis.");
					}
				}
				gera("CDIF");
				break;
			case "se":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("booleano")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("booleano")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Operador l�gico 'e' n�o aceita inteiros.");
					}
				} else {
					throw new Exception(
							"Erro na linha " + token.getLinha() + ". " + "Operador l�gico 'e' n�o aceita inteiros.");
				}
				gera("AND");
				break;
			case "sou":
				if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 1).equals("booleano")) {
					if (filaCompatibilidadeTipo.get(filaCompatibilidadeTipo.size() - 2).equals("booleano")) {
						filaCompatibilidadeTipo.remove(filaCompatibilidadeTipo.size() - 1);
						filaCompatibilidadeTipo.set(filaCompatibilidadeTipo.size() - 1, "booleano");
					} else {
						throw new Exception("Erro na linha " + token.getLinha() + ". "
								+ "Operador l�gico 'ou' n�o aceita inteiros.");
					}
				} else {
					throw new Exception(
							"Erro na linha " + token.getLinha() + ". " + "Operador l�gico 'ou' n�o aceita inteiros.");
				}
				gera("OR");
				break;
			default:
				break;
			}

		}

		filaPosfixo.removeAll(filaPosfixo);

		System.out.println("Tipo da retorno da express�o: " + filaCompatibilidadeTipo.get(0));

		return filaCompatibilidadeTipo.get(0);

	}

	public static void verificaTipoBooleano(String tipo, Token token) throws Exception {
		if (!"booleano".equals(tipo)) {
			throw new Exception("Erro na linha " + token.getLinha()
					+ ". Espera-se que o tipo de retorno da express�o seja booleano.");
		}
	}

	public static boolean verificaUnario(Token tokenAnteriorExpressao) {
		if (tokenAnteriorExpressao == null) {
			return true;
		} else if ("(".equals(tokenAnteriorExpressao.getSimbolo())) {
			return true;
		}
		return false;
	}

	public static String pesquisaTipoFuncaoVariavelTabela(String lexema) {
		int i = getUltimaPosicaoLista();
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& (NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())
					|| NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()))) {
				return tabelaSimbolos.get(i).getTipo();
			}
			i--;
		}
		return null;
	}

}
