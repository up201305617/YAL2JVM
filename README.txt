**PROJECT TITLE: P17 - SIMPLE - yal2jvm 

**GROUP: G14
NAME1: João Isaías, NR1: up201305893, GRADE1: 12, CONTRIBUTION1: 25%
NAME2: Mário Reis,  NR2: ei11135,     GRADE2: 12, CONTRIBUTION2: 25%
NAME3: Paulo Ferreira, NR3: up201305617, GRADE3: 16, CONTRIBUTION: 50%

**SUMMARY:
O compilador deve converter um programa yal em java bytecodes.
Em primeiro lugar o compilador converte o programa yal em instruções JVM. 
Na fase seguinte o assembler jasmin gera um ficheiro .class a partir do ficheiro resultante da execução do compilador. 
Por último o ficheiro .class pode ser executado através do comando java.
As principais funcionalidades são:
- Permite detetar erros sintáticos;
- Permite detetar erro semânticos;
- Permite gerar um ficheiro com instruções JVM a partir de um programa yal;
- Permite criar um ficheiro .log com os possíveis erros semânticos que o programa yal possa conter;
- Imprime na consola possíveis erros semânticos e sintáticos.

**EXECUTE:
Antes de poder executar o compilador, deve estar no diretório ./bin.
Depois de estar no diretório correto, executar o comando seguinte:

java YAL2JVM <nome_do_ficheiro>.yal

Depois de executado este comando é criado um ficheiro .j com as instruções JVM na pasta j criada dentro do diretório ./bin.
Para criar o .class através do jasmin.jar:

cd j
java -jar jasmin.jar <nome_do_ficheiro>.j

Depois de executado o comando anterior e criado um ficheiro .class, para o executar:

java <nome_do_ficheiro>

**DEALING WITH SYNTACTIC ERRORS:
Quando um erro sintático é detetado pelo compilador, para a execução, o JavaCC lança uma exceção e imprime a mensagem
de erro do JavaCC. Não consegue recuperar de um erro sintático e para a execução logo que apareça um erro.

**SEMANTIC ANALYSIS:
- Quando uma função é chamada verifica se todos os argumentos passados já foram declarados;
- Se a função pertencer ao módulo em análise e se for chamada nesse mesmo módulo, o compilador verifica se ela já
foi declarada;
- Verifica se o retorno de uma função é guardado numa variável do mesmo tipo da variável de retorno da função;
- Verifica se a variável de retorno de uma função já foi declarada;
- Verifica se o acesso a um array é do tipo Scalar ou inteiro;
- Verifica se todas as variáveis usadas durante o módulo, foram previamente declaradas;
- Quando uma função é chamada, verifica se o número de argumentos está correcto, se estão na ordem correcta e se são
do tipo correcto;
- Verifica se existem duas ou mais variáveis com o mesmo nome;
- Verifica se existem duas ou mais funções com o mesmo nome;
- Verifica se o nome da variável de retorno de uma função é diferente do nome dos argumentos;
- Verifica se um array é inicializado correctamente;
- Verifica se uma variável não guarda o valor de uma função cujo retorno é void;
- Verifica se todas as variáveis são inicializadas correctamente;
- Verifica se só os arrays têm o atributo .size;
- Nos assignments e nos conditions, verifica se todas as variáveis envolvidas foram declaradas e se correspondem aos 
tipos correctos;
- O compilador só avança para a geração de código se não ocorrerem erros semânticos;
- O compilador só termina a execução depois de todo o módulo ser analisado;
- O compilador encontra todos os erros que possam existir antes de terminar a execução.

**INTERMEDIATE REPRESENTATIONS:
Como representação intermédia é criada uma parsing tree com um LOOKHEAD = 1 e uma tabela de símbolos.
A tabela de símbolo é representada em cada módulo da seguinte forma:
- Um HashMap para as variáveis globais;
- Um HashMap para as funções.
Dentro de cada função:
- Um ArrayList para todos os argumentos da função;
- Um HashMap para as variáveis locais;
- O nome da variável de retorno e o tipo.

**CODE GENERATION:
A geração de código funciona correctamente na maioria dos casos havendo algumas exceções:
- Se variável de retorno for usada dentro dum if ou de um while, tem que ser inicializada antes de ser usada dentro 
dos ifs e dos whiles;
- Para inicializar um array com todos os elementos iguais a 1, tem que ser criado um ciclo while para inicializar o array, 
não pode simplesmente fazer: a = [4]; a=1;

**OVERVIEW:
De forma a concluir este projecto com sucesso:
- Primeiro tratou-se de criar a gramática referente à linguagem yal;
- De seguida construiu-se a tabela se símbolos das variáveis globais e das funções;
- Depois procedeu-se à análise semântica;
- E por último a geração de código.
Para auxiliar no desenvolvimento do compilador foram usadas as seguintes third-party tools:
- JavaCC;
- Jasmin.

**TESTSUITE AND TEST INFRASTRUCTURE:
Os ficheiro usados para testar o funcionamento do compilador permitem testar todas as suas capacidades, desde a descoberta 
de erros semânticos, a análise semântica e a geração de todos os tipos de estruturas de dados, ciclos e condicionais.

**TASK DISTRIBUTION:
Desenvolvimento do parser:
- Paulo Ferreira, João Isaías, Mário Reis

Árvore sintática:
- Paulo Ferreira, João Isaías, Mário Reis

Tabela de Símbolos:
- Paulo Ferreira, João Isaías, Mário Reis

Análise Semântica:
- Paulo Ferreira

Geração de Código:
- Paulo Ferreira

**PROS:
- Permite detectar erros semânticos;
- Aceita as flags -o e -r, mas avisa o utilizador que não foram implementadas;
- Se executar o compilador sem argumentos, deixa uma mensagem de como utilizar correctamente;
- Gera um ficheiro .log com o resultado das análises semânticas dos módulos.

**CONS:
- Não revela a linha do módulo onde se encontra o erro semântico, mas indica o nome da função e o nome da variável;
- Não recupera de um erro sintático;
- Tem um .limit stack de 5 em todas as funções, mas o .limit locals é calculado correctamente;
- Possui algumas exceções na geração de código (mais detalhe na secção **CODE GENERATION).