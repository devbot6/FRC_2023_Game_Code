// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import com.pathplanner.lib.commands.FollowPathWithEvents;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  

   

  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();

  // The driver's controller
  public Joystick m_driverController = new Joystick(OIConstants.kDriverControllerPort);
  public Joystick m_driverController2 = new Joystick(OIConstants.kDriverControllerPort2);

  public static final HashMap <String, Command> autoEventMap = new HashMap<>();

  // public CANSparkMax arm_motor = new CANSparkMax(18, MotorType.kBrushless);
  // public CANSparkMax arm_motor2 = new CANSparkMax(19, MotorType.kBrushless);

  // RelativeEncoder throughBoreEncoder = arm_motor.getEncoder();

  // double armEncoderPosition = throughBoreEncoder.getPosition();

  

  

  

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    m_robotDrive.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> m_robotDrive.drive(
                MathUtil.applyDeadband(-m_driverController.getRawAxis(1), 0.06),
                MathUtil.applyDeadband(-m_driverController.getRawAxis(0), 0.06),
                MathUtil.applyDeadband(-m_driverController.getRawAxis(2), 0.06),

                true),
            m_robotDrive)

            //make deadbands for 2nd controller
            
            
            
            );
  }


  



  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(m_driverController, Button.kR1.value)
        .whileTrue(new RunCommand(
            () -> m_robotDrive.setX(),
            m_robotDrive));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

// public class updateSpeed extends CommandBase{

//   public updateSpeed(Pose2d currentRobotPose){
//     Trajectory.State goal = example.sample(3.4); // sample the trajectory at 3.4 seconds from the beginning
//     ChassisSpeeds adjustedSpeeds = controller2.calculate(currentRobotPose, goal);
//   }

// }

public class stopRobot extends CommandBase{






public stopRobot(DriveSubsystem drive){
  
drive.stopAllMotors();


}


}



// public class stopRobot extends CommandBase {

  

//   private final DriveSubsystem m_drive;

//   public stopRobot(DriveSubsystem drive) {
//       m_drive = drive;
//       addRequirements(drive);
//   }

//   @Override
//   public void initialize() {
//       m_drive.setModuleStates(new SwerveModuleState[DriveConstants.kNumSwerveModules]);
//   }

//   @Override
//   public void execute() {
//       m_drive.setModuleStates(new SwerveModuleState[DriveConstants.kNumSwerveModules]);
//   }
// }


public Command newCommand(){

  var thetaController = new ProfiledPIDController(
        AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);


  autoEventMap.put("event", new PrintCommand("event passed"));



  List<PathPlannerTrajectory> autoPaths = PathPlanner.loadPathGroup("superCoolAuto", 2, 3);

  Command autoTest = new SequentialCommandGroup(
    new FollowPathWithEvents(new SwerveControllerCommand(
    autoPaths.get(0), m_robotDrive::getPose, DriveConstants.kDriveKinematics, new PIDController(AutoConstants.kPXController, 0, 0), new PIDController(AutoConstants.kPYController, 0, 0), thetaController, m_robotDrive::setModuleStates, m_robotDrive), autoPaths.get(0).getMarkers(), autoEventMap),
    new InstantCommand(m_robotDrive::stopAllMotors, m_robotDrive),
    new WaitCommand(2.5),
    new InstantCommand(m_robotDrive::stopAllMotors, m_robotDrive),
    new FollowPathWithEvents(new SwerveControllerCommand(
    autoPaths.get(1), m_robotDrive::getPose, DriveConstants.kDriveKinematics, new PIDController(AutoConstants.kPXController, 0, 0), new PIDController(AutoConstants.kPYController, 0, 0), thetaController, m_robotDrive::setModuleStates, m_robotDrive), autoPaths.get(1).getMarkers(), autoEventMap),
    new InstantCommand(m_robotDrive::stopAllMotors, m_robotDrive),
    new WaitCommand(2.5),
    new InstantCommand(m_robotDrive::stopAllMotors, m_robotDrive),
    new FollowPathWithEvents(new SwerveControllerCommand(
    autoPaths.get(2), m_robotDrive::getPose, DriveConstants.kDriveKinematics, new PIDController(AutoConstants.kPXController, 0, 0), new PIDController(AutoConstants.kPYController, 0, 0), thetaController, m_robotDrive::setModuleStates, m_robotDrive), autoPaths.get(2).getMarkers(), autoEventMap),
    new InstantCommand(m_robotDrive::stopAllMotors, m_robotDrive),
    new WaitCommand(10)
    
    
    
  
  );

  
  m_robotDrive.resetOdometry(autoPaths.get(0).getInitialPose());
  //m_robotDrive.resetOdometry(autoPaths.get(1).getInitialPose());
 
  return autoTest.andThen(() -> m_robotDrive.drive(0, 0, 0, false));
  
}


  public Command getAutonomousCommand() {

 
    PathPlannerTrajectory examplePath = PathPlanner.loadPath("autoTest", new PathConstraints(4, 3));
    //PathPlannerState exampleState = (PathPlannerState) examplePath.sample(1.2);
    //System.out.println(exampleState.velocityMetersPerSecond);
 
 
 
  //     String trajectoryJSON = "src\\main\\deploy\\pathplanner\\generatedJSONgeneratedJSON\\New Path.wpilib.json";
  //   Trajectory trajectory = new Trajectory();

  //   try {
  //     Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
  //     trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
  //  } catch (IOException ex) {
  //     DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
  //  }

    

   

    // Create config for trajectory

    //An example trajectory to follow. All units in meters.

            

            // var trajectoryOne =
            // TrajectoryGenerator.generateTrajectory(
            // new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
            // List.of(new Translation2d(1.35, 0)),
            // new Pose2d(2.7, 0, Rotation2d.fromDegrees(0)),
            // new TrajectoryConfig(AutoConstants.kMaxSpeedMetersPerSecond,
            // AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // // Add kinematics to ensure max speed is actually obeyed
            // .setKinematics(DriveConstants.kDriveKinematics));

            // var trajectoryTwo =
            // TrajectoryGenerator.generateTrajectory(
            // new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
            // List.of(new Translation2d(-1.5, 0)),
            // new Pose2d(-3, 0, Rotation2d.fromDegrees(0)),
            // new TrajectoryConfig(AutoConstants.kMaxSpeedMetersPerSecond,
            // AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // // Add kinematics to ensure max speed is actually obeyed
            // .setKinematics(DriveConstants.kDriveKinematics));

            // var trajectoryThree =
            // TrajectoryGenerator.generateTrajectory(
            // new Pose2d(3, 3, Rotation2d.fromDegrees(0)),
            // List.of(new Translation2d(1.5, 3)),
            // new Pose2d(0, 3, Rotation2d.fromDegrees(0)),
            // new TrajectoryConfig(AutoConstants.kMaxSpeedMetersPerSecond,
            // AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // // Add kinematics to ensure max speed is actually obeyed
            // .setKinematics(DriveConstants.kDriveKinematics));

            // var trajectoryFour =
            // TrajectoryGenerator.generateTrajectory(
            // new Pose2d(0, 3, Rotation2d.fromDegrees(0)),
            // List.of(new Translation2d(0, 1.5)),
            // new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
            // new TrajectoryConfig(AutoConstants.kMaxSpeedMetersPerSecond,
            // AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // // Add kinematics to ensure max speed is actually obeyed
            // .setKinematics(DriveConstants.kDriveKinematics));


            //var concatTraj = trajectoryOne;
           
            
       

  

    var thetaController = new ProfiledPIDController(
        AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
        examplePath,
        m_robotDrive::getPose, // Functional interface to feed supplier
        DriveConstants.kDriveKinematics,

    

        // Position controllers
        new PIDController(AutoConstants.kPXController, 0, 0),
        new PIDController(AutoConstants.kPYController, 0, 0),
        thetaController,
        m_robotDrive::setModuleStates,
        m_robotDrive);


    // Reset odometry to the starting pose of the trajectory.
    m_robotDrive.resetOdometry(examplePath.getInitialPose());

    // Run path following command, then stop at the end.
    return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false));
  }




 
  
}
