package com.nway.platform.workflow.service;

import java.util.List;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowElementsContainer;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

/**
 * Created by billJiang on 2017/6/22. e-mail:475572229@qq.com qq:475572229
 * 自定流程图片生成器，解决流程线条不显示文字的问题
 */
public class CustomProcessDiagramGenerator extends DefaultProcessDiagramGenerator {

	protected void drawActivity(DefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel,
			FlowNode flowNode, List<String> highLightedActivities, List<String> highLightedFlows, double scaleFactor) {

		ActivityDrawInstruction drawInstruction = activityDrawInstructions.get(flowNode.getClass());
		if (drawInstruction != null) {

			drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);

			// Gather info on the multi instance marker
			boolean multiInstanceSequential = false, multiInstanceParallel = false, collapsed = false;
			if (flowNode instanceof Activity) {
				Activity activity = (Activity) flowNode;
				MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
				if (multiInstanceLoopCharacteristics != null) {
					multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
					multiInstanceParallel = !multiInstanceSequential;
				}
			}

			// Gather info on the collapsed marker
			GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
			if (flowNode instanceof SubProcess) {
				collapsed = graphicInfo.getExpanded() != null && !graphicInfo.getExpanded();
			} else if (flowNode instanceof CallActivity) {
				collapsed = true;
			}

			if (scaleFactor == 1.0) {
				// Actually draw the markers
				processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(), (int) graphicInfo.getY(),
						(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), multiInstanceSequential,
						multiInstanceParallel, collapsed);
			}

			// Draw highlighted activities
			if (highLightedActivities.contains(flowNode.getId())) {
				drawHighLight(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
			}

		}

		// Outgoing transitions of activity
		for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
			boolean highLighted = (highLightedFlows.contains(sequenceFlow.getId()));
			String defaultFlow = null;
			if (flowNode instanceof Activity) {
				defaultFlow = ((Activity) flowNode).getDefaultFlow();
			} else if (flowNode instanceof Gateway) {
				defaultFlow = ((Gateway) flowNode).getDefaultFlow();
			}

			boolean isDefault = false;
			if (defaultFlow != null && defaultFlow.equalsIgnoreCase(sequenceFlow.getId())) {
				isDefault = true;
			}
			boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null
					&& !(flowNode instanceof Gateway);

			String sourceRef = sequenceFlow.getSourceRef();
			String targetRef = sequenceFlow.getTargetRef();
			FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
			FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
			List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
			if (graphicInfoList != null && graphicInfoList.size() > 0) {
				graphicInfoList = connectionPerfectionizer(processDiagramCanvas, bpmnModel, sourceElement,
						targetElement, graphicInfoList);
				int xPoints[] = new int[graphicInfoList.size()];
				int yPoints[] = new int[graphicInfoList.size()];

				for (int i = 1; i < graphicInfoList.size(); i++) {
					GraphicInfo graphicInfo = graphicInfoList.get(i);
					GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

					if (i == 1) {
						xPoints[0] = (int) previousGraphicInfo.getX();
						yPoints[0] = (int) previousGraphicInfo.getY();
					}
					xPoints[i] = (int) graphicInfo.getX();
					yPoints[i] = (int) graphicInfo.getY();

				}

				processDiagramCanvas.drawSequenceflow(xPoints, yPoints, drawConditionalIndicator, isDefault,
						highLighted, scaleFactor);

				// Draw sequenceflow label
				GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
				if (labelGraphicInfo != null) {
					processDiagramCanvas.drawLabel(sequenceFlow.getName(), labelGraphicInfo, false);
				} else {
					// -------------add by billJaing 2017/6/22-----------------
					GraphicInfo lineCenter = getCustomLineCenter(graphicInfoList);
					processDiagramCanvas.drawLabel(sequenceFlow.getName(), lineCenter, false);
				}
			}
		}

		// Nested elements
		if (flowNode instanceof FlowElementsContainer) {
			for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
				if (nestedFlowElement instanceof FlowNode) {
					drawActivity(processDiagramCanvas, bpmnModel, (FlowNode) nestedFlowElement, highLightedActivities,
							highLightedFlows, scaleFactor);
				}
			}
		}
	}

	protected static GraphicInfo getCustomLineCenter(List<GraphicInfo> graphicInfoList) {
		GraphicInfo gi = new GraphicInfo();

		int xPoints[] = new int[graphicInfoList.size()];
		int yPoints[] = new int[graphicInfoList.size()];

		double length = 0;
		double[] lengths = new double[graphicInfoList.size()];
		lengths[0] = 0;
		double m;
		for (int i = 1; i < graphicInfoList.size(); i++) {
			GraphicInfo graphicInfo = graphicInfoList.get(i);
			GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

			if (i == 1) {
				xPoints[0] = (int) previousGraphicInfo.getX();
				yPoints[0] = (int) previousGraphicInfo.getY();
			}
			xPoints[i] = (int) graphicInfo.getX();
			yPoints[i] = (int) graphicInfo.getY();

			length += Math.sqrt(Math.pow((int) graphicInfo.getX() - (int) previousGraphicInfo.getX(), 2)
					+ Math.pow((int) graphicInfo.getY() - (int) previousGraphicInfo.getY(), 2));
			lengths[i] = length;
		}
		m = length / 2;
		int p1 = 0, p2 = 1;
		for (int i = 1; i < lengths.length; i++) {
			double len = lengths[i];
			p1 = i - 1;
			p2 = i;
			if (len > m) {
				break;
			}
		}

		GraphicInfo graphicInfo1 = graphicInfoList.get(p1);
		GraphicInfo graphicInfo2 = graphicInfoList.get(p2);

		double AB = (int) graphicInfo2.getX() - (int) graphicInfo1.getX();
		double OA = (int) graphicInfo2.getY() - (int) graphicInfo1.getY();
		double OB = lengths[p2] - lengths[p1];
		double ob = m - lengths[p1];
		double ab = AB * ob / OB;
		double oa = OA * ob / OB;

		double mx = graphicInfo1.getX() + ab;
		double my = graphicInfo1.getY() + oa - 14;

		gi.setX(mx);
		gi.setY(my);
		return gi;
	}

	private static void drawHighLight(DefaultProcessDiagramCanvas processDiagramCanvas, GraphicInfo graphicInfo) {
		processDiagramCanvas.drawHighLight((int) graphicInfo.getX(), (int) graphicInfo.getY(),
				(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());
	}
}
