package miniplc0java.util;

import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.analyser.Analyser;
import miniplc0java.instruction.Instruction;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

//java -jar ./build/libs/miniplc0java.jar -l -o output.txt input.txt

public class PrintUtil {
    PrintStream printStream;
    private List<Byte> bytes;

    public PrintUtil (PrintStream printStream) {
        bytes = new ArrayList<Byte>();
        this.printStream = printStream;
    }

    public void print () throws IOException {
        // todo 二进制？？
//       long l = Double.doubleToLongBits(-123.312);
//       System.out.println(Long.toBinaryString(l));



        // magic
        List<Byte> magic=int2bytes(4,0x72303b3e);
        bytes.addAll(magic);

        // version
        List<Byte> version=int2bytes(4,0x00000001);
        bytes.addAll(version);

        /// 输出全局变量
        //globals.count
        List<Byte> globalCount=int2bytes(4, SymbolTable.getInstance().getGlobalCount());
        bytes.addAll(globalCount);

        //globals
        for (Symbol global: SymbolTable.getInstance().searchGlobal()) {
            // globals[0].is_const
            List<Byte> isConst=int2bytes(1, global.isConstant()?1:0);
            bytes.addAll(isConst);

            // globals[0].value.count
            List<Byte> globalValueCount;

            // globals[0].value.items
            List<Byte> globalValueItem;

            if (global.getValue() == null) {
                globalValueCount = int2bytes(4, 8);
                globalValueItem = long2bytes(8,0);
            } else {
                globalValueItem = String2bytes(global.getValue());
                globalValueCount = int2bytes(4, globalValueItem.size());
            }

            bytes.addAll(globalValueCount);
            bytes.addAll(globalValueItem);

        }

        // functions.count
        List<Byte> functionsCount=int2bytes(4, SymbolTable.getInstance().getFuncCount()-1);
        bytes.addAll(functionsCount);


        for (Instruction instruction : Analyser.instructions) {
            bytes.addAll(instruction.getBytes());
        }


        // 输出

        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); ++i) {
            result[i] = bytes.get(i);
        }
        printStream.write(result);


//        System.out.println(SymbolTable.getInstance().getGlobalCount());
//        System.out.println(SymbolTable.getInstance().searchGlobal());
//        for (Instruction instruction : Analyser.instructions) {
//            printStream.println(instruction.toString());
//        }

    }


    public static  List<Byte> String2bytes(String valueString) {
        List<Byte>  AB=new ArrayList<>();
        for (int i=0;i<valueString.length();i++){
            char ch=valueString.charAt(i);
            AB.add((byte)(ch&0xff));
        }
        return AB;
    }

    public static List<Byte> long2bytes(int length, int target) {
        ArrayList<Byte> bytes = new ArrayList<>();
        int start = 8 * (length-1);
        for(int i = 0 ; i < length; i++){
            bytes.add((byte) (( target >> ( start - i * 8 )) & 0xFF ));
        }
        return bytes;
    }

    public static ArrayList<Byte> int2bytes(int length,int target){
        ArrayList<Byte> bytes = new ArrayList<>();
        int start = 8 * (length-1);
        for(int i = 0 ; i < length; i++){
            bytes.add((byte) (( target >> ( start - i * 8 )) & 0xFF ));
        }
        return bytes;
    }

}
