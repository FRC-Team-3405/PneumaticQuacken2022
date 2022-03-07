// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
    public class Constants {
    // Power
    public static int POWER_DISTRO_ID = 0;

    
    // Sensors
    public static int PRESSURE_SENSOR_PORT = 4;

    // Cameras

    // Joysticks
    public static final int DRIVER_CONTROLLER = 0;
    public static final int DRIVER_CONTROLLER_MOVE_AXIS = 1; // Change for your controller
    public static final int DRIVER_CONTROLLER_ROTATE_AXIS = 2; // Change for your controller


    // Drive
    public static int FR_TALONSRX = 1; // Talon SRX Front Right
    public static int BR_TALONSRX = 2; // Talon SRX Back Right
    public static int FL_TALONSRX = 3; // Talon SRX Front Left
    public static int BL_TALONSRX = 4; // Talon SRX Back Left
    
    //Pneumatics
    public static int COMPRESSOR_PORT = 0; // Compressor Port
    public static int SHIFTER_IN = 0; // Double Solenoid value 1 (In)
    public static int SHIFTER_OUT = 1; // Double Solenoid value 2 (Out)
}
