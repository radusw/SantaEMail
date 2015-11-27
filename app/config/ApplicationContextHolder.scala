package config

import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * @author Radu Gancea
 */
object ApplicationContextHolder {
  /**
   * Define the spring bean application context
   */
  final val ctx = new AnnotationConfigApplicationContext()
}