package br.com.wilgner.brotreinos.exception;

public enum ErrorCode {
    USER_NOT_FOUND("Usuário não encontrado."),
    USER_ALREADY_EXISTS("Usuário já existe."),

    EXERCISE_NOT_FOUND("Exercício não encontrado."),
    EXERCISE_ALREADY_EXISTS("Já existe um exercício com esse nome"),

    TRAINING_PLAN_NOT_FOUND("Plano de treino não encontrado."),
    TRAINING_DAY_NOT_FOUND("Dia de treino não encontrado."),

    WORKOUT_SESSION_NOT_FOUND("Registro não encontrado."),
    EXECUTION_NOT_FOUND("Execução não encontrada."),

    VALIDATION_ERROR("Erro de validação."),
    UNAUTHORIZED_ACTION("Ação não autorizada."),
    INVALID_INPUT("Erro na validação dos campos"),
    BAD_FORMAT_JSON("JSON inválido ou malformado"),
    UNEXPECTED_ERROR("Erro inesperado");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
