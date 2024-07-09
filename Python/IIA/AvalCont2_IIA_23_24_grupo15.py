from MedoTotal import * 
from GrafoAbstracto import *
from collections import namedtuple
from searchPlus import *

#-------------------------------------------------------------------------------------------------------------------------------------------------
                                                                #PERGUNTA 1
#-------------------------------------------------------------------------------------------------------------------------------------------------


EstadoMedo = namedtuple('EstadoMedo', 'pacman, pastilhas, tempo, medo, visitadas, distancia')


class EstadoMedoTotal(EstadoMedo):
    """ A classe para representar a informação que muda com as acções.
    um estado é sempre considerado menor do que qualquer outro. """
    def __lt__(self,x):
        return True
    
    def __eq__(self,outro):
        return self.pacman==outro.pacman and self.pastilhas == outro.pastilhas and \
                self.visitadas == outro.visitadas and self.medo == outro.medo and self.tempo == outro.tempo
    
    def __hash__(self):
        return hash(str(self.pacman)+str(self.pastilhas)+str(self.tempo)+str(self.medo)+str(self.visitadas))


parametros="T=26\nM=6\nP=10"
linha1= "= = = = = = = = = =\n"
linha2= "= @ . * . . * . . =\n"
linha3= "= . = = = = = = . =\n"
linha4= "= . = F . . . . . =\n"
linha5= "= . = . . . . . . =\n"
linha6= "= . = . . . . . . =\n"
linha7= "= . = . . . . . . =\n"
linha8= "= * . . . . . . . =\n"
linha9= "= . . . . . . . . =\n"
linha10="= = = = = = = = = =\n"
grelha=linha1+linha2+linha3+linha4+linha5+linha6+linha7+linha8+linha9+linha10
mundoStandard=parametros + "\n" + grelha

# Distância de Manhatan entre 2 pontos
#
def manhatan(p,q):
    (x1,y1) = p
    (x2,y2) = q
    return abs(x1-x2) + abs(y1-y2)

#-------------------------------------------------------------------------------------------------------------------------------------------------

#-------------------------------------------------------------------------------------------------------------------------------------------------


# A subclasse de Problem: 
#
class Turbo(Problem):
    

    def __init__(self, initial):
        x,y,z = initial #posicao atual, lista de posições nao visitadas (contem pacman, pastilhas e casas vazias), posicao objetivo 
        self.initial = x,y,set(),z


    def actions(self, state):
        pos, path, visited, _ = state
        (x,y) = pos
        inPath = set()

        if (x,y-1) in path and (x,y-1) not in visited:#vê se pode ir para cima, baixo, esq e dir
            inPath.add((x,y-1)) 
        if (x-1,y) in path and (x-1,y) not in visited:
            inPath.add((x-1,y))
        if (x+1,y) in path and (x+1,y) not in visited:
            inPath.add((x+1,y))
        if (x,y+1) in path and (x,y+1) not in visited:
            inPath.add((x,y+1))
        return inPath #retorna os indices (i,j) para onde se pode mover (e não N W E S)
    
    def result(self, state, action):
        pos, path, visited, goalPos = state                     
        visited.add(pos)                  #remove a pos onde está das posiçoes nao visitadas
        pos = action                      #atualiza a posicao onde esta para a da action recebida.


        return (pos, path, visited, goalPos) #retorna o estado
    
    def goal_test(self, state):
        pos, _, visited, goalPos = state
        return pos == goalPos
    
    def path_cost(self, c, state1, action, state2):
        return c + 1
    
#-------------------------------------------------------------------------------------------------------------------------------------------------

#-------------------------------------------------------------------------------------------------------------------------------------------------


# A subclasse de Problem: MedoTotal
#
class MedoTotalTurbo(Problem):
    """Encontrar um caminho numa grelha 2D com obstáculos. Os obstáculos são células (x, y)."""

    def conv_txt_estado(self,txt):
    
        def processa_linha(dados,y,linha):
            x=0
            for c in linha:
                if c=='*':
                    dados['pastilhas'].add((x,y))
                elif c=='=':
                    dados['obstaculos'].add((x,y))
                elif c=='@':
                    dados['pacman']=(x,y)
                elif c=='F':
                    dados['fantasma']=(x,y)
                if c=='.':
                    dados['caminho'].add((x,y))
                if c!= " ":
                    x+=1

        
        linhas=txt.split('\n')
        T=int(linhas[0][2:])
        M=int(linhas[1][2:])
        P=int(linhas[2][2:])
        dim=(len(linhas[3])+1)//2
        
        dados={'dim':dim, 'T':T, 'M':M, 'P':P, 'obstaculos':set(), 'pastilhas':set(), 'caminho': set()}
        y=0
        for l in linhas[3:]:
            processa_linha(dados,y,l)
            y+=1
        return dados
    
    def caminho(self,possiblePaths,gums):
        allDistanceToGum = {}
        
        for p in possiblePaths:
            distanceToGums = []
            for g in gums:
                
                turbo = Turbo((p, possiblePaths, g))


                resultado = breadth_first_tree_search(turbo)
                resultado2 = depth_first_graph_search(turbo)
                
                if resultado2 == None:
                    cost = resultado.path_cost
                elif resultado == None:
                    cost = resultado2.path_cost
                elif resultado > resultado2:
                    cost = resultado2.path_cost
                else:
                    cost = resultado.path_cost
                if cost > 0:                        #ver se é necessário este if 
                    distanceToGums.append(cost)
                distanceToGums.sort()
            allDistanceToGum.update({p : distanceToGums})
        return allDistanceToGum
            
        

        
    def __init__(self, texto_input=mundoStandard):
        diccio=self.conv_txt_estado(texto_input)
        
        self.goal=diccio['T'] 
        self.fantasma = diccio['fantasma']
        self.poder = diccio['P']
        self.obstacles=diccio['obstaculos']
        self.dim=diccio['dim']
        dados = diccio['caminho']
        dados.add(diccio['pacman']) #add posicao do pacman
        for g in diccio['pastilhas']:#add posicao das pastilhas 
            dados.add(g)
        self.dados = dados
        self.gumDistance = self.caminho(dados, diccio['pastilhas'])
        self.initial=EstadoMedoTotal(diccio['pacman'], diccio['pastilhas'], diccio['T'], diccio['M'],{diccio['pacman']:1}, self.gumDistance) 


    directions = {"N":(0, -1), "W":(-1, 0), "E":(+1,  0),"S":(0, +1)}  # ortogonais
    
                  
    def result(self, state, action): 
        "Tanto as acções como os estados são representados por pares (x,y)."
        pacman,pastilhas,tempo,medo,visitadas, distancia=state
        (x,y) = pacman
        (dx,dy) = self.directions[action]
        npos = (x+dx,y+dy)

        if npos == self.fantasma:
            medo=0
        elif npos in pastilhas:
            pastilhas = pastilhas - {npos}
            medo = self.poder
            
            exp = self.caminho(self.dados, pastilhas)
            distancia = exp.copy()
        else:
            medo -= 1
        tempo -=1
        copia_visitadas = visitadas.copy()
        freq=copia_visitadas.get(npos,0)
        copia_visitadas[npos]=freq+1

        return(EstadoMedoTotal(npos, pastilhas, tempo, medo, copia_visitadas, distancia))
    
    

    # situações de falha antecipada
    #
    def falha_antecipada(self,state):
        if state.tempo <= state.medo:
            return False
        if state.pastilhas == set(): # se não há mais pastilhas e eram necessárias
            return True
        minDist = -1

        if (len(self.gumDistance.get(state.pacman)) > 0):
            minDist = state.distancia.get(state.pacman)[0]
        if minDist > state.medo: # se não há tempo (manhatan) para chegar à próxima super-pastilha
            return True
        if (state.medo + self.poder * len(state.pastilhas)) < state.tempo:
            # se o poder de todas as pastilhas mais o medo são insuficientes.
            return True
        return False
    
    def actions(self, state):
        """Podes mover-te para uma célula em qualquer das direcções para uma casa 
           que não seja obstáculo nem fantasma."""
        x, y = state.pacman
        return [act for act in self.directions.keys() 
                if (x+self.directions[act][0],y+self.directions[act][1]) not in (self.obstacles | {self.fantasma}) and 
                not self.falha_antecipada(self.result(state,act))]

    
    def path_cost(self,c,state,action,new):
        return c + new.visitadas[new.pacman]
    
    def goal_test(self,state):
        return state.tempo==0

    def display(self, state):
        """ print the state please"""
        output="Tempo: "+ str(state.tempo) + "\n"
        output+="Medo: "+ str(state.medo)  + "\n"
        for j in range(self.dim):
            for i in range(self.dim):
                if state.pacman ==(i,j):
                    ch = '@'
                elif self.fantasma==(i,j):
                    ch = "F"
                elif (i,j) in self.obstacles:
                    ch = "="
                elif (i,j) in state.pastilhas:
                    ch = '*'
                else:
                    ch = "."
                output += ch + " "
            output += "\n"
        return output  

    def executa(p,estado,accoes,verbose=False):
        """Executa uma sequência de acções a partir do estado devolvendo o triplo formado pelo estado, 
        pelo custo acumulado e pelo booleano que indica se o objectivo foi ou não atingido. Se o objectivo for atingido
        antes da sequência ser atingida, devolve-se o estado e o custo corrente.
        Há o modo verboso e o não verboso, por defeito."""
        custo = 0
        for a in accoes:
            seg = p.result(estado,a)
            custo = p.path_cost(custo,estado,a,seg)
            estado = seg
            objectivo=p.goal_test(estado)
            if verbose:
                p.display(estado)
                print('Custo Total:',custo)
                print('Atingido o objectivo?', objectivo)
            if objectivo:
                break
        return (estado,custo,objectivo)
    
    def minimal_h(self,node):
        return node.state.tempo




#-------------------------------------------------------------------------------------------------------------------------------------------------
                                                                #PERGUNTA 2
#-------------------------------------------------------------------------------------------------------------------------------------------------




def func_Best_State(problem, expanded_nodes, best_solution, num_visited_states, num_final_states, optimization,verbose):
        for no in expanded_nodes:
            if best_solution is None:
                best_solution = no
                num_visited_states +=1
                num_final_states += 1
                if verbose:
                    func_verbose(problem, no.state, no, best_solution, True)

            elif optimization and no.path_cost < best_solution.path_cost:
                num_visited_states +=1  
                num_final_states += 1
                best_solution = no
                if verbose:
                    func_verbose(problem, no.state, no, best_solution, True)
            elif not optimization:
                num_final_states += 1
                num_visited_states +=1
                if(no.path_cost < best_solution.path_cost):
                    best_solution = no
                    if verbose:
                        func_verbose(problem, no.state, no, best_solution, True)
                else:
                    if verbose:
                        func_verbose(problem, no.state, no, best_solution, False)
        return best_solution, num_visited_states, num_final_states 

def func_verbose(problem, state, node, best_solution, bestValue):


    if problem.goal_test(state):
        print("---------------------\n")
        print(problem.display(state))
        print(f"GGGGooooooallllll --------- com o custo: {best_solution.path_cost}")
        if(bestValue):
            print("Di best goal até agora")
    else:
        print("---------------------\n")
        print(problem.display(state))
        print(f"Custo: {node.path_cost}")

def depth_first_tree_search_all_count(problem, optimization=False, verbose=False):  
    num_visited_states = 0
    max_frontier_size = 0
    num_final_states = 0
    best_solution = None
    
    frontier = [Node(problem.initial)]
    
    while frontier:
        max_frontier_size = max(max_frontier_size, len(frontier))
        node = frontier.pop()
        num_visited_states += 1

        if verbose:
            func_verbose(problem, node.state, node, best_solution, False)

        expanded_nodes = node.expand(problem)
        #verifica se é vazia a as acoes
        if expanded_nodes:
            #se nao for estado final adiciona a fronteira
            if not problem.goal_test(expanded_nodes[0].state):
                if not optimization:
                    frontier.extend(reversed(expanded_nodes))
                else:
                    if best_solution is None:
                        frontier.extend(reversed(expanded_nodes))
                    else:
                        lista = []
                        for no in expanded_nodes:
                            if best_solution.path_cost > no.path_cost:
                                lista.append(no)
                        if(len(lista) > 0):
                            frontier.extend(reversed(lista))
            #Caso contrario adiciona a lista de estados finais
            else:
                best_solution,num_visited_states,num_final_states  = func_Best_State(problem, expanded_nodes, best_solution, num_visited_states, num_final_states, optimization,verbose)

            
    
    return best_solution, max_frontier_size, num_visited_states, num_final_states



#-------------------------------------------------------------------------------------------------------------------------------------------------
                                                                #PERGUNTA 3
#-------------------------------------------------------------------------------------------------------------------------------------------------




def ida_star_graph_search_count(problem, f, verbose=False):
    num_visitados_estados = 0
    solucao = None
    cut_of = f(Node(problem.initial))
    min_cut_of = float('inf')
    newCutOf = False
    visited_nodes = set()
    visited_nodes.add(problem.initial)
    fronteira = [Node(problem.initial)]
    if(verbose):
        print ("------Cutoff at", cut_of, "\n")

    while fronteira:
        verboseDone = False
        no = fronteira.pop()
        num_visitados_estados += 1


        if(problem.goal_test(no.state) and f(no) <= cut_of and solucao == None):
            if(verbose):
                print(no.state)
                print ("Cost:", no.path_cost, "f=", f(no))
                print("Goal found within cutoff!")
            solucao = no


        if(cut_of < f(no) < min_cut_of):
            newCutOf = True
            min_cut_of = f(no)
            if(verbose):
                print(no.state)
                print ("Cost:", no.path_cost, "f=", f(no))
                print ("Out of cutoff -- minimum out:", min_cut_of, "\n")
                verboseDone = True


        if(not problem.goal_test(no.state) and cut_of >= f(no) and solucao == None):

            expanded_nodes = reversed(no.expand(problem))

            for node in expanded_nodes:
                if(node.state not in visited_nodes):
                    visited_nodes.add(node.state)
                    fronteira.append(node)



        if(verbose and not verboseDone and not fronteira and not newCutOf and solucao == None):
            print(no.state)
            print ("Cost:", no.path_cost, "f=", f(no))
            verboseDone = True


        if(verbose and not verboseDone and solucao == None):
            print(no.state)
            print ("Cost:", no.path_cost, "f=", f(no), "\n")


        if(cut_of < min_cut_of and not fronteira and newCutOf and solucao == None):
            newCutOf = False
            cut_of = min_cut_of
            fronteira = [Node(problem.initial)]
            visited_nodes = set()
            visited_nodes.add(problem.initial)
            min_cut_of = float('inf')
            if(verbose):
                print()
                print ("------Cutoff at", cut_of, "\n")

    return (solucao, num_visitados_estados)