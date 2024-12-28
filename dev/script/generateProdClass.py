#!/usr/bin/env python3

import os

target_path = "/home/david.ferrari@primeurad.com/coding/java/jlox/src/main/java/com/primeur/parser"
expression_package_name = "com.primeur.parser"
token_package_name = "com.primeur.lexer"


ast = {
        "Binary" : "Expr left, Token operator, Expr right",
        "Grouping" : "Expr right",
        "Literal" : "Object value",
        "Unary" : "Token operator, Expr right" 
        }

def expression_package_statement():
   return f"package {expression_package_name};\n"

def import_token_statement():
    return f"import {token_package_name}.Token;\n"

def expr_class_header(expr):
    return f"public class {expr} {{\n"

def field_declaration(field):
    return f"\tprivate final {field.strip()};\n"

def constructor(expr, field_string):
    def field_assignment():
        res = ""
        field_list = field_string.split(",")
        for i, field in enumerate(field_list):
            field_name = field.strip().split(" ")[1]
            res += f"\t\tthis.{field_name} = {field_name};"
            if i != len(field_list) - 1:
                res += "\n"
        return res

    return f"\tpublic {expr}({field_string}) {{\n{field_assignment()}\n\t}}\n"

for expr in ast:
    field_string = ast[expr]
    with open(os.path.join(target_path, f"{expr}.java"), "w") as file:
        file.write(expression_package_statement())
        file.write('\n')

        if "Token" in field_string:
            file.write(import_token_statement())
            file.write('\n')
        file.write(expr_class_header(expr))

        for field in field_string.split(","):
            file.write(field_declaration(field))

        file.write('\n')
        file.write(constructor(expr, field_string))
        file.write("}\n")
