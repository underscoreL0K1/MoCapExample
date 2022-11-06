// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private XboxController m_DriveController;

  private WPI_TalonFX m_leftMotorOne;
  private WPI_TalonFX m_leftMotorTwo;
  private WPI_TalonFX m_leftMotorThree;

  private WPI_TalonFX m_RightMotorFour;
  private WPI_TalonFX m_RightMotorFive;
  private WPI_TalonFX m_RightMotorSix;

  private MotorControllerGroup left;
  private MotorControllerGroup right;

  private DifferentialDrive drive;


  static int x; 
  public static File XControl;
  public static File YControl;
  static FileWriter XRecord;
  static FileWriter YRecord;
  static double xJoy;
  static double yJoy;
  static String xString;
  static String yString;
  static BufferedReader XlinReader;
  static BufferedReader YlinReader;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_DriveController = new XboxController(0);


    m_leftMotorOne = new WPI_TalonFX(1);
    m_leftMotorTwo = new WPI_TalonFX(2);
    m_leftMotorThree = new WPI_TalonFX(3);

    m_RightMotorFour = new WPI_TalonFX(4);
    m_RightMotorFive = new WPI_TalonFX(5);
    m_RightMotorSix = new WPI_TalonFX(6);


    m_leftMotorOne.setNeutralMode(NeutralMode.Brake);
    m_leftMotorTwo.setNeutralMode(NeutralMode.Brake);
    m_leftMotorThree.setNeutralMode(NeutralMode.Brake);
    
    m_RightMotorFour.setNeutralMode(NeutralMode.Brake);
    m_RightMotorFive.setNeutralMode(NeutralMode.Brake);
    m_RightMotorSix.setNeutralMode(NeutralMode.Brake);


    right = new MotorControllerGroup(m_RightMotorFour, m_RightMotorFive, m_RightMotorSix);
    left = new MotorControllerGroup(m_leftMotorThree, m_leftMotorOne, m_leftMotorTwo);

    left.setInverted(true);

    drive = new DifferentialDrive(right, left);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    XControl = new File("/home/lvuser/delpoy/Xpos.txt");
    YControl = new File("/home/lvuser/deploy/Ypos.txt");
    try {
      YlinReader = new BufferedReader(new FileReader(YControl));
      XlinReader = new BufferedReader(new FileReader(XControl));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }


    //Drives the prerecorded path by reading the saved file
    try{
      yString = YlinReader.readLine();
      xString = XlinReader.readLine();

      yJoy = Double.valueOf(yString);
      xJoy = Double.valueOf(xString);

      drive.arcadeDrive(yJoy, xJoy);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    XControl = new File("/home/lvuser/Xpos.txt");
    YControl = new File("/home/lvuser/Ypos.txt");

    try {
      XRecord = new FileWriter(XControl);
      YRecord = new FileWriter(YControl);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /** This function is called periodically during test mode. */


  //Run this function to record an autopath by driving
  @Override
  public void testPeriodic() {

    yJoy = m_DriveController.getLeftY();
    xJoy = m_DriveController.getRightX();
   
    yString = String.valueOf(yJoy) + "\n";
    xString = String.valueOf(xJoy) + "\n";
    

    try {
      YRecord.append(yString);
      YRecord.flush();
      XRecord.append(xString);
      XRecord.flush();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  
    drive.arcadeDrive(yJoy, xJoy);

  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
