package com.example.linux.pdfPublisheru.publisher;

public class ConfluenceAuthentication {


    String userName = "user";
    char[] password = "password".toCharArray();

//        AuthenticatedWebResourceProvider provider = AuthenticatedWebResourceProvider.createWithNewClient(url.getUrl());
//provider.setAuthContext(username, password);
//
//    ListeningExecutorService listeningExecutorService = createListeningExecutorService();
//    RemoteContentService service = new RemoteContentServiceImpl(getProvider(), listeningExecutorService);
//    AuthenticatedWebResourceProvider client = new AuthenticatedWebResourceProvider(RestClientFactory.newClient(),
//            getConfluenceCredential().getConfluenceURI(), null);
//
//client.setAuthContext(getConfluenceCredential().getUserName(),
//    getConfluenceCredential().getUserPassword().toCharArray());
//
//    Executor executor = new ThreadPoolExecutor(1,5,1,TimeUnit.MINUTES, new ArrayBlockingQueue(2));
//
//    private User getUser() {
//        return AuthenticatedUserThreadLocal.get();
//    }
}
