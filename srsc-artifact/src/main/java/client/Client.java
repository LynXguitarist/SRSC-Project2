package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = in.readLine();
			String operation = line.split(" ")[0];
			// rest of the line -> after the operation
			String controls = line.substring(operation.length());
			switch (operation) {
			case "ls":
				operationLs(controls);
				break;
			case "Login":
				operationLogin(controls);
				break;
			case "mkdir":
				operationMkdir(controls);
				break;
			case "put":
				operationPut(controls);
				break;
			case "get":
				operationGet(controls);
				break;
			case "cp":
				operationCp(controls);
				break;
			case "rm":
				operationRm(controls);
				break;
			case "rmdir":
				operationRmdir(controls);
				break;
			case "file":
				operationFile(controls);
				break;
			default:
				return;
			}
		}
	}

	private static void operationLs(String controls) {
		// mostra ficheiros ou diretórios do utilizador username na sua home-root
	}

	private static void operationLogin(String controls) {
		String username = controls.split(" ")[0];
		String passowrd = controls.split(" ")[1];
	}

	private static void operationMkdir(String controls) {
		// cria um directório na path indicada
	}

	private static void operationPut(String controls) {
		// coloca um ficheiro file na path indicada
	}

	private static void operationGet(String controls) {
		// obtem ficheiro file na path indicada
	}

	private static void operationCp(String controls) {
		// copia ficheiro file 1 na path 1 para file 2 na path 2
	}

	private static void operationRm(String controls) {
		// apaga ficheiro file na path indicada
	}

	private static void operationRmdir(String controls) {
		// apaga ficheiro file na path indicada
	}

	private static void operationFile(String controls) {
		/*
		 * Mostra atributos do ficheiro file na path indicada, devolvendo o nome, se é
		 * um diretório ou se é um ficheiro, o tipo de ficheiro, data da criação, data
		 * da última modificação
		 */
	}

}
