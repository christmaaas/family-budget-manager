#include <jni.h>
#include <vector>
#include <string>
#include <sstream>

#include <jni.h>

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_familybudgetmanager_fragments_AddTransactionFragment_calculateNewBudget(
        JNIEnv *env,
        jobject instance,
        jdouble currentBudget,
        jdouble amount,
        jboolean isProfit) {
    if (isProfit) {
        return currentBudget + amount;
    } else {
        return currentBudget - amount;
    }
}
