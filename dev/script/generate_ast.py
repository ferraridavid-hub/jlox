#!/usr/bin/env python3

import os

def define_base_class(target, basename):
    path = os.path.join(target, f"{basename}.java")
    with open(path, "w") as writer:
        writer.write("package com.primeur.parser.ast;\n")
        writer.write("\n")
        writer.write(f"public abstract class {basename} {{\n")
        writer.write(f"\tpublic abstract <R> R accept({basename}Visitor<R> visitor);\n")
        writer.write("}\n")


def define_visitor(target, basename, productions):
    path = os.path.join(target, f"{basename}Visitor.java")
    with open(path, "w") as writer:
        writer.write("package com.primeur.parser.ast;\n")
        writer.write("\n")
        writer.write(f"public interface {basename}Visitor<R> {{\n")
        for prod_name in productions:
            classname = prod_name + basename
            arg_name = prod_name.lower() + basename
            writer.write(f"\tR visit{classname}({classname} {arg_name});\n")
        writer.write("}\n")


def define_production_classes(target, basename, productions):
    for prod_name, prod_fields in productions.items():
        classname = prod_name + basename
        path = os.path.join(target, f"{classname}.java")
        with open(path, "w") as writer:
            writer.write("package com.primeur.parser.ast;\n")
            writer.write("\n")

            # import Token class if required
            if prod_fields and ("Token" in prod_fields):
                writer.write("import com.primeur.lexer.Token;\n")
                writer.write("\n")

            if prod_fields and ("List" in prod_fields):
                writer.write("import java.util.List;\n")
                writer.write("\n")

            writer.write(f"public class {classname} extends {basename} {{\n")
            writer.write("\n")

            # private fields constructor and getter methods
            if prod_fields:
                fields = prod_fields.split(",")
                for field in fields:
                    writer.write(f"\tprivate final {field.strip()};\n")

                writer.write("\n")
                writer.write(f"\tpublic {classname}({prod_fields.strip()}) {{\n")
                for field in fields:
                    field_type, field_name = [s.strip() for s in field.strip().split(" ")]
                    writer.write(f"\t\tthis.{field_name} = {field_name};\n")
                writer.write("\t}\n")
                writer.write("\n")

                for field in fields:
                    field_type, field_name = [s.strip() for s in field.strip().split(" ")]
                    writer.write(f"\t public {field_type} get{field_name[0].upper() + field_name[1:]}() {{\n")
                    writer.write(f"\t\treturn this.{field_name};\n")
                    writer.write("\t}\n")
                    writer.write("\n")


            # accept method
            writer.write("\t@Override\n")
            writer.write(f"\tpublic <R> R accept({basename}Visitor<R> visitor) {{\n")
            writer.write(f"\t\treturn visitor.visit{classname}(this);\n")
            writer.write("\t}")
            writer.write("\n")

            writer.write("}\n")

def define_ast(target, basename, productions):
    define_base_class(target, basename)
    define_visitor(target, basename, productions)
    define_production_classes(target, basename, productions)

if __name__ == "__main__":
    output_dir = "../../src/main/java/com/primeur/parser/ast"
    expression_productions = {
        "Assign": "Token name, Expr value",
        "Binary" : "Expr left, Token operator, Expr right",
        "Grouping" : "Expr expression",
        "Literal" : "Object value",
        "Unary" : "Token operator, Expr right", 
        "Ternary": "Expr left, Token leftOperator, Expr middle, Token rightOperator, Expr right",
        "Variable": "Token name"
        }
    define_ast(output_dir, "Expr", expression_productions)

    statement_productions = {
        "Block": "List<Stmt> statements",
        "Expression": "Expr expression",
        "Print": "Expr expression",
        "Var": "Token name, Expr initializer"
    }
    define_ast(output_dir, "Stmt", statement_productions)

