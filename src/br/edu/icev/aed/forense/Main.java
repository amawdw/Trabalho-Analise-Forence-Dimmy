package br.edu.icev.aed.forense;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        AnalistaForense analistaForense = new AnalistaForense();
        try {
            System.out.println(analistaForense.encontrarPicosTransferencia("arquivo_logs.csv"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
