1) CORRER O PROGRAMA

- 	Abrir cmd na diretoria do projeto

- 	Escrever <java -jar "out/artifacts/java2_0_jar/java2.0.jar">

-   Caso o programa não corra devido a um erro do Jython, ir a: File -> Project Structure -> Modules -> + (à direita) -> JARs or directories... ->
    tp2/CALIC/jython2.7.2/jython.jar -> Ok -> clicar no jython.rar na lista das dependências -> Apply -> Ok

2) SOBRE O CODEC "CALIC"

- 	Não corre pelo command prompt.

- 	Necessita do Jython para funcionar, uma vez que a função a ser importada para Java está em Python (instalação já efetuada).

- 	Existem alguns problemas do Jython que complicam o uso de funções de path e directories em ambos os programas. 
	Devido a isso, o ficheiro .py necessita de estar em "./TI Codecs Program", assim como os inputs (imagens originais). 
	Os outputs do CALIC vão necessariamente estar nessa pasta também.

- 	O programa corre tanto em Python como em Java. Para correr em Python, os inputs necessitam de estar em "./TI Codecs Program/CALIC/calic/"
	onde se encontra o ficheiro .py. Para correr em Java os inputs e o ficheiro .py necessitam de estar em "./TI Codecs Program". 
	O programa não corre o codec se não reunir estas condições (dependendo do IDE onde pretendemos correr).

- 	O Jython também tem um problema que não nos permite visualizar os prints do ficheiro .py no ficheiro .java. 
	Dado isso, não conseguimos visualizar o tempo de compressão nem a entropia.
	
3) Outras informações

	Devido às opções limitadas de compressão e ao tamanho da entrega ter que ser inferior a 100, tivemos que dividir as compressões em várias 
	chunks para poder entregar o projeto.