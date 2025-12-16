package org.example;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Метод-анализатор, который считает количество условных операторов IF, циклов,
 * инструкций в циклах и объявлений переменных в одном методе класса.
 */
public class MethodAnalyzer {
    
    /** Количество условных операторов IF */
    public int ifCount = 0;
    
    /** Количество циклов (все backward jumps + switch) */
    public int loopCount = 0; 
    
    /** Количество инструкций, которые относятся к циклам */
    public int loopInstructions = 0;

    /** Количество объявлений локальных переменных */
    public int variableCount = 0;
    
    /**
     * Анализирует метод, подсчитывает IF, циклы, инструкции циклов и объявления переменных.
     *
     * @param method метод ASM, который нужно проанализировать
     */
    public void analyze(MethodNode method) {
        // === Подсчет реальных переменных ===
        if (method.localVariables != null) {
            Set<String> counted = new HashSet<>();
            for (LocalVariableNode var : method.localVariables) {
                // Игнорируем скрытые переменные this и this$*
                if (!"this".equals(var.name) && !var.name.startsWith("this$") && counted.add(var.name)) {
                    variableCount++;
                }
            }
        }

        InsnList insns = method.instructions;
        if (insns == null || insns.size() == 0) return;

        Set<LabelNode> countedLoops = new HashSet<>();

        for (int i = 0; i < insns.size(); i++) {
            AbstractInsnNode insn = insns.get(i);

            // === IF (условные переходы вперед) ===
            if (insn instanceof JumpInsnNode jump) {
                int opcode = jump.getOpcode();
                boolean backward = isBackwardJump(insns, jump, i);

                // условный переход вперёд => IF
                if (!backward && opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) {
                    ifCount++;
                }

                // === Цикл (backward jump) ===
                if (backward && !countedLoops.contains(jump.label)) {
                    loopCount++;
                    countedLoops.add(jump.label);

                    // считаем инструкции от начала цикла до jump
                    loopInstructions += i - insns.indexOf(jump.label) + 1;
                }
            }

            // === Switch тоже как цикл ===
            else if (insn instanceof TableSwitchInsnNode ts) {
                loopCount++;
                loopInstructions += ts.labels.size() + 1;
            } else if (insn instanceof LookupSwitchInsnNode ls) {
                loopCount++;
                loopInstructions += ls.labels.size() + 1;
            }
        }
    }

    /**
     * Проверяет, является ли jump инструкцией, которая идет назад по коду.
     *
     * @param insns список инструкций метода
     * @param jump инструкция перехода
     * @param currentIndex индекс текущей инструкции
     * @return true, если jump указывает назад по коду, иначе false
     */
    private boolean isBackwardJump(InsnList insns, JumpInsnNode jump, int currentIndex) {
        for (int j = 0; j < insns.size(); j++) {
            if (insns.get(j) == jump.label) {
                return j < currentIndex;
            }
        }
        return false;
    }

    /**
     * Сбрасывает все счетчики к нулю.
     * Используется перед повторным анализом другого метода.
     */
    public void reset() {
        ifCount = 0;
        loopCount = 0;
        loopInstructions = 0;
        variableCount = 0;
    }
}
