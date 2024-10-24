cmake_minimum_required(VERSION 3.10.2)

project("familybudgetmanager")

add_library(
        budget_calculator
        SHARED
        BudgetCalculator.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        budget_calculator
        ${log-lib})
