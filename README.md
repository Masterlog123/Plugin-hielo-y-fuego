# Hielo y Fuego

Este plugin aplica efectos negativos a los jugadores dependiendo del mundo en el que se encuentren y su equipamiento.

## Versiones soportadas
Minecraft 1.21.4 (Paper o Spigot).

## Compilación

El proyecto usa Maven. Para compilar el plugin:

```bash
mvn package
```

El artefacto se generará en `target/HieloYFuego-1.0.0.jar`.

## Configuración

El archivo `config.yml` permite definir:

- Mundos especiales (`mundos.hielo` y `mundos.fuego`).
- Equipamiento requerido para anular los efectos en cada mundo.

Si una pieza se define como `NONE`, no se requerirá esa parte de la armadura.

Coloca el plugin compilado y el `config.yml` en la carpeta `plugins` de tu servidor.
