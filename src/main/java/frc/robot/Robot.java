// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.PigeonIMU;

// note
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  // Right Motors
  private final WPI_TalonSRX m_front_right = new WPI_TalonSRX(Constants.FR_TALONSRX);
  private final WPI_TalonSRX m_back_right = new WPI_TalonSRX(Constants.BR_TALONSRX);
  // Left Motors
  private final WPI_TalonSRX m_front_left = new WPI_TalonSRX(Constants.FL_TALONSRX);
  private final WPI_TalonSRX m_back_left = new WPI_TalonSRX(Constants.BL_TALONSRX);
  // Motor Controller Groups
  private final MotorControllerGroup rightMotors = new MotorControllerGroup(m_front_right, m_back_right);
  private final MotorControllerGroup leftMotors = new MotorControllerGroup(m_front_left, m_back_left);
  // Differential Drive
  private final DifferentialDrive drivetrain = new DifferentialDrive(rightMotors,leftMotors);
  
  // Joystick
  private final Joystick stick = new Joystick(0);
  // Xbox Controller
  private final XboxController xbox = new XboxController(0);
  // Pneumatics
  private final Compressor comp = new Compressor(Constants.COMPRESSOR_PORT, PneumaticsModuleType.CTREPCM);
  private final DoubleSolenoid shifter = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,4,5);

  //Limelight
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry ta = table.getEntry("ta");

  //Pigeon 2
  PigeonIMU _pigeon = new PigeonIMU(0);
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    rightMotors.setInverted(true);
    leftMotors.setInverted(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    //Zero the encoders
    m_back_left.setSelectedSensorPosition(0);
    m_back_right.setSelectedSensorPosition(0);
    
    /*m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    
    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
    */
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);

    if(_loopCount_right++ > 10)
    {
        _loopCount_right = 0;
        double r_degrees = m_back_right.getSelectedSensorPosition(2);
        //System.out.println("CANCoder position is: " + degrees);
        SmartDashboard.putNumber("Right CANCoder:", r_degrees);

        //Pigeon 2
        double[] ypr = new double[3];
        _pigeon.getYawPitchRoll(ypr);
        SmartDashboard.putNumber("Pigeon Yaw is: ", ypr[0]);
    
    }
    if(_loopCount_left++ > 10)
    {
        _loopCount_left = 0;
        double l_degrees = m_back_left.getSelectedSensorPosition(4);
        SmartDashboard.putNumber("Left CANCoder: ", l_degrees);
    }
    if (Math.abs(m_back_left.getSelectedSensorPosition()) < Constants.AUTONOMOUS_DISTANCE) {
      leftMotors.set(.5);
    }
    if (Math.abs(m_back_right.getSelectedSensorPosition()) < Constants.AUTONOMOUS_DISTANCE) {
      rightMotors.set(.5);
    }
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }
  int _loopCount_right = 0;
  int _loopCount_left = 0;
  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);

    drivetrain.arcadeDrive(stick.getX(),stick.getY());

    if (stick.getRawButtonPressed(1)) {
      shifter.set(DoubleSolenoid.Value.kForward);
    }
    else if (stick.getRawButtonPressed(2)) {
      shifter.set(DoubleSolenoid.Value.kReverse);
    }
    if(_loopCount_right++ > 10)
    {

        _loopCount_right = 0;
        double r_degrees = m_back_right.getSelectedSensorPosition(2);
        //System.out.println("CANCoder position is: " + degrees);
        SmartDashboard.putNumber("Right CANCoder:", r_degrees);

        //Pigeon 2
        double[] ypr = new double[3];
            _pigeon.getYawPitchRoll(ypr);
            SmartDashboard.putNumber("Pigeon Yaw is: ", ypr[0]);

    }
    if(_loopCount_left++ > 10)
    {
        _loopCount_left = 0;
        double l_degrees = m_back_left.getSelectedSensorPosition(4);
        SmartDashboard.putNumber("Left CANCoder: ", l_degrees);
    }

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
