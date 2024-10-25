#include <jni.h>
#include <vector>
#include <string>
#include <sstream>
#include <iomanip>

#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_familybudgetmanager_fragments_AddTransactionFragment_calculateNewBudget(
        JNIEnv *env,
        jobject instance,
        jdouble currentBudget,
        jdouble amount,
        jboolean isProfit) {
    double newBudget;
    if (isProfit) {
        newBudget = currentBudget + amount;
    } else {
        newBudget = currentBudget - amount;
    }

    std::stringstream stream;
    stream << std::fixed << std::setprecision(2) << newBudget;
    std::string formattedBudget = stream.str();

    return env->NewStringUTF(formattedBudget.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_familybudgetmanager_fragments_AddTransactionFragment_updateIncomeOrExpense(
        JNIEnv *env,
        jobject instance,
        jdouble currentIncome,
        jdouble currentExpense,
        jdouble amount,
        jboolean isProfit) {
    double updatedValue;
    if (isProfit) {
        updatedValue = currentIncome + amount;
    } else {
        updatedValue = currentExpense + amount;
    }

    std::stringstream stream;
    stream << std::fixed << std::setprecision(2) << updatedValue;
    std::string result = stream.str();

    return env->NewStringUTF(result.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_familybudgetmanager_fragments_History_calculateNewBudget(
        JNIEnv *env,
        jobject instance,
        jdouble currentBudget,
        jdouble amount,
        jboolean isProfit) {
    double newBudget;
    if (isProfit) {
        newBudget = currentBudget - amount;
    } else {
        newBudget = currentBudget + amount;
    }

    std::stringstream stream;
    stream << std::fixed << std::setprecision(2) << newBudget;
    std::string formattedBudget = stream.str();

    return env->NewStringUTF(formattedBudget.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_familybudgetmanager_fragments_History_updateIncomeOrExpense(
        JNIEnv *env,
        jobject instance,
        jdouble currentIncome,
        jdouble currentExpense,
        jdouble amount,
        jboolean isProfit) {
    double updatedValue;
    if (isProfit) {
        updatedValue = currentIncome - amount;
    } else {
        updatedValue = currentExpense - amount;
    }

    std::stringstream stream;
    stream << std::fixed << std::setprecision(2) << updatedValue;
    std::string result = stream.str();

    return env->NewStringUTF(result.c_str());
}
