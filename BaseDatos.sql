-- Esquema de Base de Datos para Microservicios Bancarios

-- Tabla para Clientes (hereda de Persona)
CREATE TABLE IF NOT EXISTS clientes (
  id UUID PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  genero VARCHAR(50),
  edad INT,
  identificacion VARCHAR(255) NOT NULL UNIQUE,
  direccion VARCHAR(255),
  telefono VARCHAR(50),
  contrasena VARCHAR(255) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Tabla para Cuentas
CREATE TABLE IF NOT EXISTS cuentas (
  id UUID PRIMARY KEY,
  numero VARCHAR(50) UNIQUE NOT NULL,
  tipo VARCHAR(50) NOT NULL,
  saldo_inicial NUMERIC(18, 2) NOT NULL,
  saldo NUMERIC(18, 2) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  cliente_id UUID NOT NULL,
  CONSTRAINT fk_cliente
    FOREIGN KEY(cliente_id) 
    REFERENCES clientes(id)
    ON DELETE CASCADE
);

-- Tabla para Movimientos
CREATE TABLE IF NOT EXISTS movimientos (
  id UUID PRIMARY KEY,
  cuenta_id UUID NOT NULL,
  fecha TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  tipo_movimiento VARCHAR(50),
  valor NUMERIC(18, 2) NOT NULL,
  saldo_resultante NUMERIC(18, 2) NOT NULL,
  descripcion VARCHAR(255),
  CONSTRAINT fk_cuenta
    FOREIGN KEY(cuenta_id) 
    REFERENCES cuentas(id)
    ON DELETE CASCADE
);

-- Tabla para el patrón Outbox (eventos de dominio)
CREATE TABLE IF NOT EXISTS outbox_event (
  id UUID PRIMARY KEY,
  aggregate_type VARCHAR(255) NOT NULL,
  aggregate_id VARCHAR(255) NOT NULL,
  event_type VARCHAR(255) NOT NULL,
  payload TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Datos de ejemplo

-- Clientes
INSERT INTO clientes (id, nombre, genero, edad, identificacion, direccion, telefono, contrasena, estado)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'Jose Lema', 'Masculino', 35, '1701234567', 'Otavalo sn y principal', '098254785', '1234', TRUE)
ON CONFLICT (identificacion) DO NOTHING;

INSERT INTO clientes (id, nombre, genero, edad, identificacion, direccion, telefono, contrasena, estado)
VALUES
  ('22222222-2222-2222-2222-222222222222', 'Marianela Montalvo', 'Femenino', 28, '1709876543', 'Amazonas y NNUU', '097548965', '5678', TRUE)
ON CONFLICT (identificacion) DO NOTHING;

-- Cuentas
INSERT INTO cuentas (id, numero, tipo, saldo_inicial, saldo, estado, cliente_id)
VALUES
  ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '478758', 'Ahorros', 2000, 1425, TRUE, '11111111-1111-1111-1111-111111111111')
ON CONFLICT (numero) DO NOTHING;

INSERT INTO cuentas (id, numero, tipo, saldo_inicial, saldo, estado, cliente_id)
VALUES
  ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '225487', 'Corriente', 100, 700, TRUE, '22222222-2222-2222-2222-222222222222')
ON CONFLICT (numero) DO NOTHING;

-- Movimientos
INSERT INTO movimientos (id, cuenta_id, fecha, tipo_movimiento, valor, saldo_resultante, descripcion)
VALUES
  ('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1', 'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', NOW() - INTERVAL '2 days', 'Deposito', 600.00, 700.00, 'Deposito de 600')
ON CONFLICT (id) DO NOTHING;

INSERT INTO movimientos (id, cuenta_id, fecha, tipo_movimiento, valor, saldo_resultante, descripcion)
VALUES
  ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', NOW() - INTERVAL '1 day', 'Retiro', -575.00, 1425.00, 'Retiro de 575')
ON CONFLICT (id) DO NOTHING;