import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


public class WebDownload {
    public static void main(String[] args) {

        // Pedir la web al usuario
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce la web (por ejemplo www.myweb.com): ");
        String url = sc.nextLine();
        // Añadir http:// delante de la url
        url = "http://" + url;

        // Crear el cliente y la petición
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Enviar la petición y mostrar la respuesta
        Future<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                return client.send(request,HttpResponse.BodyHandlers.ofString()).body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((response, error) -> {
            System.out.println("\nRespuesta: \n\n" + response + "\n\nCompletado");
        });

        // Esperar a que se resuelva la petición
        while (!future.isDone()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
