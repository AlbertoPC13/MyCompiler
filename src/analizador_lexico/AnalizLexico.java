package analizador_lexico;

import java.util.Stack;

public class AnalizLexico {

    int token, EdoActual, EdoTransicion;
    String CadenaSigma;
    public String Lexema;
    boolean PasoPorEdoAcept;
    int IniLexema, FinLexema, IndiceCaracterActual;
    char CaracterActual;
    Stack<Integer> Pila = new Stack<>();
    AFD AutomataFD;

    public AnalizLexico() {
        CadenaSigma = "";
        PasoPorEdoAcept = false;
        IniLexema = -1;
        FinLexema = -1;
        IndiceCaracterActual = -1;
        token = -1;
        Pila.clear();
        AutomataFD = null;
    }

    public AnalizLexico(String sigma, String FileAFD, int IdAFD) {
        AutomataFD = new AFD();
        CadenaSigma = sigma;
        PasoPorEdoAcept = false;
        IniLexema = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        token = -1;
        Pila.clear();
        AutomataFD.ImportarAFD(FileAFD, IdAFD);
    }

    public AnalizLexico(String sigma, String FileAFD) {
        AutomataFD = new AFD();
        CadenaSigma = sigma;
        PasoPorEdoAcept = false;
        IniLexema = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        token = -1;
        Pila.clear();
        AutomataFD.ImportarAFD(FileAFD, -1);
    }

    public AnalizLexico(String FileAFD, int IdAFD) {
        AutomataFD = new AFD();
        CadenaSigma = "";
        PasoPorEdoAcept = false;
        IniLexema = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        token = -1;
        Pila.clear();
        AutomataFD.ImportarAFD(FileAFD, IdAFD);
    }

    public AnalizLexico(String sigma, AFD AutFD) {
        CadenaSigma = sigma;
        PasoPorEdoAcept = false;
        IniLexema = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        token = -1;
        Pila.clear();
        AutomataFD = AutFD;
    }

    public void SetSigma(String sigma) {
        CadenaSigma = sigma;
        PasoPorEdoAcept = false;
        IniLexema = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        token = -1;
        Pila.clear();
    }

    public int yylex() {
        while (true) {
            Pila.push(IndiceCaracterActual);
            if (IndiceCaracterActual >= CadenaSigma.length()) {
                Lexema = "";
                return SimbolosEspeciales.FIN;
            }

            IniLexema = IndiceCaracterActual;
            EdoActual = 0;
            PasoPorEdoAcept = false;
            FinLexema = -1;
            token = -1;

            while (IndiceCaracterActual < CadenaSigma.length()) {
                CaracterActual = CadenaSigma.charAt(IndiceCaracterActual);
                EdoTransicion = AutomataFD.TablaAFD[EdoActual][CaracterActual];
                if (EdoTransicion != -1) {
                    if (AutomataFD.TablaAFD[EdoTransicion][256] != -1) {
                        PasoPorEdoAcept = true;
                        token = AutomataFD.TablaAFD[EdoTransicion][256];
                        FinLexema = IndiceCaracterActual;
                    }
                    IndiceCaracterActual++;
                    EdoActual = EdoTransicion;
                    continue;
                }
                break;
            }
            if (!PasoPorEdoAcept) {
                IndiceCaracterActual = IniLexema + 1;
                Lexema = CadenaSigma.substring(IniLexema, IniLexema + 1);
                token = SimbolosEspeciales.ERROR;
                return token;
            }
            Lexema = CadenaSigma.substring(IniLexema, FinLexema + 1);
            IndiceCaracterActual = FinLexema + 1;
            if (token == SimbolosEspeciales.OMITIR)
                continue;
            else
                return token;
        }
    }

    public boolean UndoToken() {
        if (Pila.empty()) {
            return false;
        }
        IndiceCaracterActual = Pila.pop();
        return true;
    }

    public EstadoAnalizLexico GetEdoAnalizLexico() {
        EstadoAnalizLexico EdoActualAnaliz = new EstadoAnalizLexico();
        EdoActualAnaliz.CaracterActual = CaracterActual;
        EdoActualAnaliz.EdoTransicion = EdoTransicion;
        EdoActualAnaliz.FinLexema = FinLexema;
        EdoActualAnaliz.IndiceCaracterActual = IndiceCaracterActual;
        EdoActualAnaliz.IniLexema = IniLexema;
        EdoActualAnaliz.Lexema = Lexema;
        EdoActualAnaliz.PasoPorEdoAcept = PasoPorEdoAcept;
        EdoActualAnaliz.token = token;
        EdoActualAnaliz.Pila = Pila;

        return EdoActualAnaliz;
    }

    public boolean SetEdoAnalizLexico(EstadoAnalizLexico e) {
        CaracterActual = e.CaracterActual;
        EdoActual = e.EdoActual;
        EdoTransicion = e.EdoTransicion;
        FinLexema = e.FinLexema;
        IndiceCaracterActual = e.IndiceCaracterActual;
        IniLexema = e.IniLexema;
        Lexema = e.Lexema;
        PasoPorEdoAcept = e.PasoPorEdoAcept;
        token = e.token;
        Pila = e.Pila;

        return true;
    }
}
