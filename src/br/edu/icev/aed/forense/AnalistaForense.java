package br.edu.icev.aed.forense;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AnalistaForense implements AnaliseForenseAvancada {
    @Override
    public Set<String> encontrarSessoesInvalidas(String caminhoArquivo) throws IOException {


        Map<String, Stack<String>> sessoes = new HashMap<>();
        Set<String> sessoesInvalidas = new HashSet<>();

        //criar um leitor de linhas que vai ler as linas do arquivo de 'log'
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            /*
             * Criar uma variavel booleana para o cabeçalho para
             * poder pular a primeira linha usando um tatamento facil de if els
             */
            boolean cabecalho = true;

            //Ler até a leitura da linha retornar o vazio, ou seja, acabar o arquivo
            while ((linha = br.readLine()) != null) {

                //Metodo citado anteriormente para pular o cabeçalho(primeira linha)
                if (cabecalho) {
                    cabecalho = false;
                    continue;
                }

                //o fim de cada coluna é uma vírgula, então o split vai ser a vírgula
                String[] coluna = linha.split(",");

                String sessionId = coluna[2].trim();
                String actionType = coluna[3].trim();

                //começo da resolução do desafio propriamente dito
                if (actionType.equals("LOGIN")) {
                    //Adquiri os valores da pilha se não estiver vazia
                    Stack<String> pilha = sessoes.get(sessionId);
                    //caso contrário(estiver vazia) cria uma nova
                    if (pilha == null) {
                        pilha = new Stack<>();
                        sessoes.put(sessionId, pilha);
                    }

                    //Empilha o ‘login’ quando encontrar
                    pilha.push(actionType);

                    //Tratando os casos de ‘login’ sem logout correspondente
                    if (pilha.size() > 1) {
                        //Anotar o ‘ID’ da sessção que fez ‘login’ sem 'deslogar'
                        sessoesInvalidas.add(sessionId);
                    }

                } else if (actionType.equals("LOGOUT")) {
                    //Adquiri os valores da pilha se não estiver vazia
                    Stack<String> pilha = sessoes.get(sessionId);

                    //Tratando os casos de 'logout' sem ‘login’ correspondente
                    if (pilha == null || pilha.isEmpty()) {
                        //Anotar o ‘ID’ da sessção que fez ‘logout’ sem 'deslogar'
                        sessoesInvalidas.add(sessionId);
                    } else {
                        // Remove o ‘login’ do topo da pilha (desempilha)
                        pilha.pop();
                    }
                }

            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            throw e;
        }

        // Percorrendo todas as sessões abertas no Map
        for (Map.Entry<String, Stack<String>> entry : sessoes.entrySet()) {
            // Se a pilha não está vazia, logo, faltou 'logout' (sessão inválida)
            if (!entry.getValue().isEmpty()) {
                sessoesInvalidas.add(entry.getKey());  // Adiciona ao Set de inválidas
            }
        }

        return sessoesInvalidas;
    }

    @Override
    public List<String> reconstruirLinhaTempo(String caminhoArquivo, String sessionId) throws IOException {
        //usei uma fila pra manter a ordem cronologica das açoes
        Deque<String> fila = new ArrayDeque<>();

        // abre o arquivo pra leitura
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeira = true; //pra pular o cabeçalho se tiver

            while ((linha = br.readLine()) != null) {
                if (linha.isEmpty()) continue;

                // pula a primeira linha se for o cabeçalho do CSV
                if (primeira && linha.toUpperCase(Locale.ROOT).startsWith("TIMESTAMP,")) {
                    primeira = false;
                    continue;
                }
                primeira = false;

                //separa as linhas das colunas
                String[] cols = linha.split(",", 7);
                if (cols.length < 7) continue; //se < 7 ignora

                if (!sessionId.equals(cols[2])) continue;
                fila.addLast(cols[3]);
            }
        }
        //transforma fila em lista na ordem certinha
        List<String> resultado = new ArrayList<>(fila.size());
        while (!fila.isEmpty()) resultado.add(fila.removeFirst());
        return resultado;
    }


    @Override
    public List<Alerta> priorizarAlertas(String caminhoArquivo, int n) throws IOException {
        return List.of();
    }

    @Override
    public Map<Long, Long> encontrarPicosTransferencia(String caminhoArquivo) throws IOException {
        List<Long> timestamps = new ArrayList<>();
        List<Long> bytesList = new ArrayList<>();
        Map<Long, Long> picos = new HashMap<>();
        Stack<Integer> pilha = new Stack<>();
        // Leitura do arquivo (mesmo código anterior)
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean cabecalho = true;
            while ((linha = br.readLine()) != null) {
                if (cabecalho) {
                    cabecalho = false;
                    continue;
                }
                String[] coluna = linha.split(",");
                Long timestamp = Long.parseLong(coluna[0].trim());
                Long bytes = Long.parseLong(coluna[6].trim());
                timestamps.add(timestamp);
                bytesList.add(bytes);
            }
        }

        // Percorre de trás para frente
        for (int i = timestamps.size() - 1; i >= 0; i--) {
            long bytesAtual = bytesList.get(i);

            // Remove da pilha todos com bytes MENORES ou IGUAIS ao atual
            while (!pilha.isEmpty() && bytesList.get(pilha.peek()) <= bytesAtual) {
                pilha.pop();
            }

            // Se a pilha não está vazia, o topo é o próximo maior
            if (!pilha.isEmpty()) {
                picos.put(timestamps.get(i), timestamps.get(pilha.peek()));
            }

            // Adiciona o índice atual à pilha
            pilha.push(i);
        }

        return picos;
    }

    @Override
    public Optional<List<String>> rastrearContaminacao(String caminhoArquivo, String recursoInicial, String recursoAlvo) throws IOException {
        return Optional.empty();
    }
}


