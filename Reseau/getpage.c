#include <netdb.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <netinet/in.h>

int main (int argc, char **argv){
  int port = 80;
  if (argc != 2)
    perror("manque argument");

  struct hostent *host;
  host = gethostbyname(argv[1]);
  if (host == NULL) {
    perror("host inconnu");
    return -1;
  }
  
  if (host->h_addrtype == AF_INET){
    struct in_addr *adres;
    adres =  (struct in_addr*) host->h_addr;
    //printf("%x\n",adres->s_addr);


    int fd = socket(AF_INET,SOCK_STREAM,0);
    if (fd < 0)
      perror("socket");
    
    struct sockaddr_in sockad;
    sockad.sin_family = AF_INET;
    sockad.sin_port = htons(port);
    sockad.sin_addr = *adres;
    if ( connect(fd,(struct sockaddr *) &sockad,sizeof(sockad)) <0){
      perror("connect");
      close(fd);
      return -1;
    }
    FILE *open = fdopen(fd,"r+");
    fprintf(open,"GET / HTTP/1.0 \nHost: %s \n\n",argv[1]);
    fflush(open);
    char buffer[101];
    
    while ((fgets(buffer,100,open)) != 0){
       buffer[100] ='\0';
       printf("%s",buffer);
    }
  }
  return 0;
}
