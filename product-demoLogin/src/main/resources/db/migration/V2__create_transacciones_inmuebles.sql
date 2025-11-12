-- Crear tabla de historial de transacciones de inmuebles
CREATE TABLE IF NOT EXISTS transacciones_inmuebles (
    id BIGSERIAL PRIMARY KEY,
    estado_anterior VARCHAR(50),
    estado_actual VARCHAR(50) NOT NULL,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT NOW(),
    inmueble_id BIGINT NOT NULL,
    cliente_id BIGINT,
    comentario_agente TEXT,
    CONSTRAINT fk_trans_inmueble FOREIGN KEY (inmueble_id) REFERENCES inmueble(id) ON DELETE CASCADE,
    CONSTRAINT fk_trans_cliente FOREIGN KEY (cliente_id) REFERENCES users(id) ON DELETE SET NULL
);

