**PROJECT TITLE: P17 - SIMPLE - yal2jvm 

**GROUP: G14
NAME1: Jo�o Isa�as, NR1: up201305893, GRADE1: 12, CONTRIBUTION1: 25%
NAME2: M�rio Reis,  NR2: ei11135,     GRADE2: 12, CONTRIBUTION2: 25%
NAME3: Paulo Ferreira, NR3: up201305617, GRADE3: 16, CONTRIBUTION: 50%

**SUMMARY:
O compilador deve converter um programa yal em java bytecodes.
Em primeiro lugar o compilador converte o programa yal em instru��es JVM. 
Na fase seguinte o assembler jasmin gera um ficheiro .class a partir do ficheiro resultante da execu��o do compilador. 
Por �ltimo o ficheiro .class pode ser executado atrav�s do comando java.
As principais funcionalidades s�o:
- Permite detetar erros sint�ticos;
- Permite detetar erro sem�nticos;
- Permite gerar um ficheiro com instru��es JVM a partir de um programa yal;
- Permite criar um ficheiro .log com os poss�veis erros sem�nticos que o programa yal possa conter;
- Imprime na consola poss�veis erros sem�nticos e sint�ticos.

**EXECUTE:
Antes de poder executar o compilador, deve estar no diret�rio ./bin.
Depois de estar no diret�rio correto, executar o comando seguinte:

java YAL2JVM <nome_do_ficheiro>.yal

Depois de executado este comando � criado um ficheiro .j com as instru��es JVM na pasta j criada dentro do diret�rio ./bin.
Para criar o .class atrav�s do jasmin.jar:

cd j
java -jar jasmin.jar <nome_do_ficheiro>.j

Depois de executado o comando anterior e criado um ficheiro .class, para o executar:

java <nome_do_ficheiro>

**DEALING WITH SYNTACTIC ERRORS:
Quando um erro sint�tico � detetado pelo compilador, para a execu��o, o JavaCC lan�a uma exce��o e imprime a mensagem
de erro do JavaCC. N�o consegue recuperar de um erro sint�tico e para a execu��o logo que apare�a um erro.

**SEMANTIC ANALYSIS:
- Quando uma fun��o � chamada verifica se todos os argumentos passados j� foram declarados;
- Se a fun��o pertencer ao m�dulo em an�lise e se for chamada nesse mesmo m�dulo, o compilador verifica se ela j�
foi declarada;
- Verifica se o retorno de uma fun��o � guardado numa vari�vel do mesmo tipo da vari�vel de retorno da fun��o;
- Verifica se a vari�vel de retorno de uma fun��o j� foi declarada;
- Verifica se o acesso a um array � do tipo Scalar ou inteiro;
- Verifica se todas as vari�veis usadas durante o m�dulo, foram previamente declaradas;
- Quando uma fun��o � chamada, verifica se o n�mero de argumentos est� correcto, se est�o na ordem correcta e se s�o
do tipo correcto;
- Verifica se existem duas ou mais vari�veis com o mesmo nome;
- Verifica se existem duas ou mais fun��es com o mesmo nome;
- Verifica se o nome da vari�vel de retorno de uma fun��o � diferente do nome dos argumentos;
- Verifica se um array � inicializado correctamente;
- Verifica se uma vari�vel n�o guarda o valor de uma fun��o cujo retorno � void;
- Verifica se todas as vari�veis s�o inicializadas correctamente;
- Verifica se s� os arrays t�m o atributo .size;
- Nos assignments e nos conditions, verifica se todas as vari�veis envolvidas foram declaradas e se correspondem aos 
tipos correctos;
- O compilador s� avan�a para a gera��o de c�digo se n�o ocorrerem erros sem�nticos;
- O compilador s� termina a execu��o depois de todo o m�dulo ser analisado;
- O compilador encontra todos os erros que possam existir antes de terminar a execu��o.

**INTERMEDIATE REPRESENTATIONS:
Como representa��o interm�dia � criada uma parsing tree com um LOOKHEAD = 1 e uma tabela de s�mbolos.
A tabela de s�mbolo � representada em cada m�dulo da seguinte forma:
- Um HashMap para as vari�veis globais;
- Um HashMap para as fun��es.
Dentro de cada fun��o:
- Um ArrayList para todos os argumentos da fun��o;
- Um HashMap para as vari�veis locais;
- O nome da vari�vel de retorno e o tipo.

**CODE GENERATION:
A gera��o de c�digo funciona correctamente na maioria dos casos havendo algumas exce��es:
- Se vari�vel de retorno for usada dentro dum if ou de um while, tem que ser inicializada antes de ser usada dentro 
dos ifs e dos whiles;
- Para inicializar um array com todos os elementos iguais a 1, tem que ser criado um ciclo while para inicializar o array, 
n�o pode simplesmente fazer: a = [4]; a=1;

**OVERVIEW:
De forma a concluir este projecto com sucesso:
- Primeiro tratou-se de criar a gram�tica referente � linguagem yal;
- De seguida construiu-se a tabela se s�mbolos das vari�veis globais e das fun��es;
- Depois procedeu-se � an�lise sem�ntica;
- E por �ltimo a gera��o de c�digo.
Para auxiliar no desenvolvimento do compilador foram usadas as seguintes third-party tools:
- JavaCC;
- Jasmin.

**TESTSUITE AND TEST INFRASTRUCTURE:
Os ficheiro usados para testar o funcionamento do compilador permitem testar todas as suas capacidades, desde a descoberta 
de erros sem�nticos, a an�lise sem�ntica e a gera��o de todos os tipos de estruturas de dados, ciclos e condicionais.

**TASK DISTRIBUTION:
Desenvolvimento do parser:
- Paulo Ferreira, Jo�o Isa�as, M�rio Reis

�rvore sint�tica:
- Paulo Ferreira, Jo�o Isa�as, M�rio Reis

Tabela de S�mbolos:
- Paulo Ferreira, Jo�o Isa�as, M�rio Reis

An�lise Sem�ntica:
- Paulo Ferreira

Gera��o de C�digo:
- Paulo Ferreira

**PROS:
- Permite detectar erros sem�nticos;
- Aceita as flags -o e -r, mas avisa o utilizador que n�o foram implementadas;
- Se executar o compilador sem argumentos, deixa uma mensagem de como utilizar correctamente;
- Gera um ficheiro .log com o resultado das an�lises sem�nticas dos m�dulos.

**CONS:
- N�o revela a linha do m�dulo onde se encontra o erro sem�ntico, mas indica o nome da fun��o e o nome da vari�vel;
- N�o recupera de um erro sint�tico;
- Tem um .limit stack de 5 em todas as fun��es, mas o .limit locals � calculado correctamente;
- Possui algumas exce��es na gera��o de c�digo (mais detalhe na sec��o **CODE GENERATION).